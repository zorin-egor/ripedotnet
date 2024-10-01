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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


internal class InetNumRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val inetNumsDao: InetNumsDao,
    @IoScope private val scope: CoroutineScope
) : InetNumRepository {

    override fun getInetNumByIp(ip: String): Flow<Result<InetNum?>> =
        flow<Result<InetNum?>> {
            Timber.d("getInetNumByIp($ip)")

            Timber.d("getInetNumByIp() - db")
            inetNumsDao.getInetNumByIp(ip = ip)
                .take(1)
                .mapNotNull { it?.toInetNumModel() }
                .map { Result.success(it) }
                .catch { Timber.e(t = it, message = "dao") }
                .collect(::emit)

            Timber.d("getInetNumByIp() - network request")
            val response = networkDatasource.getSearchByIp(query = ip)
                .getResultOrThrow()

            val resultModel = response?.networkToInetNumModels()
                ?.firstOrNull()
                ?.takeIf { it.orgId != null }

            emit(Result.success(resultModel))

            Timber.d("getInetNumByIp() - db write")
            response?.networkToInetNumEntities()
                ?.map { it.copy(queryIp = ip) }
                ?.let {
                    scope.launch {
                        runCatching { inetNumsDao.insertAll(it) }
                            .exceptionOrNull()?.let(Timber::e)
                    }
                }

            Timber.d("getInetNumByIp() - end")
        }.catch { emit(Result.failure<InetNum?>(it)) }
            .distinctUntilChanged()

    override fun getInetNumsByOrgId(id: String, offset: Int, limit: Int): Flow<Result<List<InetNum>>> =
        flow<Result<List<InetNum>>>{
            Timber.d("getOrgInetNumsById($id)")

            Timber.d("getOrgInetNumsById() - db")
            inetNumsDao.getInetNumsByOrgId(orgId = id, offset = offset, limit = limit)
                .take(1)
                .filterNot { it.isEmpty() }
                .map { Result.success(it.toInetNumModels()) }
                .catch { Timber.e(it) }
                .collect(::emit)

            Timber.d("getOrgInetNumsById() - network request")
            val response = networkDatasource.getOrgNetworks(query = id, offset = offset, limit = limit)
                    .getResultOrThrow()

            val resultModels = response?.networkToInetNumModels()
                ?.map { it.copy(orgId = id) } ?: emptyList()

            emit(Result.success(resultModels))

            Timber.d("getOrgInetNumsById() - db write")
            response?.networkToInetNumEntities()
                ?.map { it.copy(orgId = id) }
                ?.let {
                    scope.launch {
                        runCatching { inetNumsDao.insertAll(it) }
                            .exceptionOrNull()?.let(Timber::e)
                    }
                }

            Timber.d("getOrgInetNumsById() - end")
        }.catch { emit(Result.failure<List<InetNum>>(it)) }
            .distinctUntilChanged()

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
