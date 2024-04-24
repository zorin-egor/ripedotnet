package com.sample.ripedotnet.core.data.repositories.netnum

import com.sample.ripedotnet.core.data.models.networkToInetNumEntities
import com.sample.ripedotnet.core.data.models.networkToInetNumModels
import com.sample.ripedotnet.core.data.models.toInetNumModel
import com.sample.ripedotnet.core.data.models.toInetNumModels
import com.sample.ripedotnet.core.database.dao.InetNumsDao
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.NetworkDataSource
import com.sample.ripedotnet.core.network.di.IoScope
import com.sample.ripedotnet.core.network.ext.getResultOrThrow
import com.sample.ripedotnet.core.network.retrofit.SELF_IP_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


internal class InetNumRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val inetNumsDao: InetNumsDao,
    @IoScope private val scope: CoroutineScope
) : InetNumRepository {

    override fun getInetNumByIp(ip: String): Flow<InetNum?> =
        flow<InetNum?> {
            Timber.d("getInetNumByIp($ip)")

            Timber.d("getInetNumByIp() - db")
            var item: InetNum? = null
            inetNumsDao.getInetNumByIp(ip = ip)
                .take(1)
                .catch { Timber.e(t = it, message = "dao") }
                .mapNotNull { it?.toInetNumModel() }
                .onEach { item = it }
                .collect(::emit)

            Timber.d("getInetNumByIp() - network request")
            val response = networkDatasource.getSearchByIp(query = ip)
                .getResultOrThrow()

            val resultModel = response?.networkToInetNumModels()
                ?.firstOrNull()
                ?.takeIf { it.orgId != null }

            if (item != null && item == resultModel) {
                return@flow
            }

            emit(resultModel)

            Timber.d("getInetNumByIp() - db write")
            response?.networkToInetNumEntities()
                ?.map { it.copy(queryIp = ip) }
                ?.let { scope.launch {
                    runCatching { inetNumsDao.insertAll(it) }
                        .exceptionOrNull()
                        ?.let(Timber::e)
                }}

            Timber.d("getInetNumByIp() - end")
        }

    override fun getInetNumsByOrgId(id: String, offset: Int, limit: Int): Flow<List<InetNum>> =
        flow<List<InetNum>>{
            Timber.d("getOrgInetNumsById($id)")

            Timber.d("getOrgInetNumsById() - db")
            val items = mutableListOf<InetNum>()
            inetNumsDao.getInetNumsByOrgId(orgId = id, offset = offset, limit = limit)
                .take(1)
                .catch { Timber.e(it) }
                .mapNotNull { entities ->
                    entities.takeIf { it.isNotEmpty() }
                        ?.toInetNumModels()
                }
                .onEach(items::addAll)
                .collect(::emit)

            Timber.d("getOrgInetNumsById() - network request")
            val response = networkDatasource.getOrgNetworks(query = id, offset = offset, limit = limit)
                    .getResultOrThrow()

            val resultModel = response?.networkToInetNumModels()
                ?.map { it.copy(orgId = id) }
                ?: emptyList()

            if (items.isNotEmpty() && items == resultModel) {
                return@flow
            }

            emit(resultModel)

            Timber.d("getOrgInetNumsById() - db write")
            response?.networkToInetNumEntities()
                ?.map { it.copy(orgId = id) }
                ?.let { scope.launch {
                    runCatching { inetNumsDao.insertAll(it) }
                        .exceptionOrNull()
                        ?.let(Timber::e)
                }}

            Timber.d("getOrgInetNumsById() - end")
        }

    override fun getSelfIp(): Flow<String?> =
        flow {
            val result = networkDatasource.getUrlFree(
                url = SELF_IP_URL,
                headers = mapOf("User-Agent" to "Mozilla/5.0")
            )
            emit(result.body())
        }.catch {
            Timber.e(it)
            emit(null)
        }
}
