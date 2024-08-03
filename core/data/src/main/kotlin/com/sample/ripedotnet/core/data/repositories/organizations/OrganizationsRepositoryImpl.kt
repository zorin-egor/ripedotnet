package com.sample.ripedotnet.core.data.repositories.organizations

import com.sample.ripedotnet.core.data.models.networkToOrganizationModel
import com.sample.ripedotnet.core.data.models.networkToOrganizationsEntities
import com.sample.ripedotnet.core.data.models.toOrgModel
import com.sample.ripedotnet.core.data.models.toOrgModels
import com.sample.ripedotnet.core.data.models.toOrganizationEntity
import com.sample.ripedotnet.core.database.dao.OrganizationsDao
import com.sample.ripedotnet.core.model.logic.Organization
import com.sample.ripedotnet.core.network.NetworkDataSource
import com.sample.ripedotnet.core.network.di.IoScope
import com.sample.ripedotnet.core.network.ext.getResultOrThrow
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

internal class OrganizationsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val organizationsDao: OrganizationsDao,
    @IoScope private val scope: CoroutineScope
) : OrganizationsRepository {

    override fun getOrganizationsByName(name: String, offset: Int, limit: Int): Flow<List<Organization>> =
        flow<List<Organization>> {
            Timber.d("getOrganizationsByName($name, $offset, $limit)")

            Timber.d("getOrganizationsByName() - db")
            val items = mutableListOf<Organization>()
            organizationsDao.getOrganizations(query = name, offset = offset, limit = limit)
                .take(1)
                .mapNotNull { entities ->
                    entities.takeIf { it.isNotEmpty() }
                        ?.toOrgModels()
                }
                .onEach(items::addAll)
                .catch { Timber.e(it) }
                .collect(::emit)

            Timber.d("getOrganizationsByName() - network request")
            val response = networkDatasource.getSearchByName(query = name, offset = offset, limit = limit)
                .getResultOrThrow()

            val result = response?.networkToOrganizationModel() ?: emptyList()

            if (items.isNotEmpty() && items == result) {
                return@flow
            }

            emit(result)

            Timber.d("getOrganizationsByName() - db write")
            response?.networkToOrganizationsEntities()
                ?.let { scope.launch {
                    runCatching { organizationsDao.insertAll(it) }
                        .exceptionOrNull()
                        ?.let(Timber::e)
                }}

            Timber.d("getOrganizationsByName() - end")
        }

    override fun getOrganizationById(id: String): Flow<Organization?> =
        flow<Organization?> {
            Timber.d("getOrganizationById($id)")

            Timber.d("getOrganizationById() - db")
            var item: Organization? = null
            organizationsDao.getOrganizationById(id = id)
                .take(1)
                .mapNotNull { it?.toOrgModel() }
                .onEach { item = it }
                .catch { Timber.e(it) }
                .collect(::emit)

            Timber.d("getOrganizationById() - network request")
            val response = networkDatasource.getOrgDetails(orgId = id).getResultOrThrow()
            val result = response?.networkToOrganizationModel()?.firstOrNull()

            if (item != null && item == result) {
                return@flow
            }

            emit(result)

            Timber.d("getOrganizationById() - db write")
            response?.networkToOrganizationsEntities()
                ?.let { scope.launch {
                    runCatching { organizationsDao.insertAll(it) }
                        .exceptionOrNull()
                        ?.let(Timber::e)
                }}

            Timber.d("getOrganizationById() - end")
        }

    override suspend fun add(item: Organization) = organizationsDao.insert(item.toOrganizationEntity())

    override suspend fun remove(item: Organization) = organizationsDao.delete(item.toOrganizationEntity())

}

