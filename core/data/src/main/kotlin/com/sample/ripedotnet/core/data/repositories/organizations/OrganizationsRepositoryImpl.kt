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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
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
            organizationsDao.getOrganizations(query = name, offset = offset, limit = limit)
                .take(1)
                .catch { Timber.e(it) }
                .mapNotNull { entities ->
                    entities.takeIf { it.isNotEmpty() }
                        ?.toOrgModels()
                }
                .collect(::emit)

            Timber.d("getOrganizationsByName() - network request")
            val response = networkDatasource.getSearchByName(
                query = name,
                offset = offset,
                limit = limit
            ).getResultOrThrow()

            emit(response?.networkToOrganizationModel() ?: emptyList())

            Timber.d("getOrganizationsByName() - db write")
            response?.networkToOrganizationsEntities()
                ?.let { scope.launch { runCatching { organizationsDao.insertAll(it) }}}

            delay(5000)

            Timber.d("getOrganizationsByName() - end")
        }

    override fun getOrganizationById(id: String): Flow<Organization?> =
        flow<Organization?> {
            Timber.d("getOrganizationById($id)")

            Timber.d("getOrganizationById() - db")
            organizationsDao.getOrganizationById(id = id)
                .take(1)
                .catch { Timber.e(it) }
                .mapNotNull { it?.toOrgModel() }
                .collect(::emit)

            Timber.d("getOrganizationById() - network request")
            val response = networkDatasource.getOrgDetails(orgId = id)
                .getResultOrThrow()

            emit(response?.networkToOrganizationModel()?.firstOrNull())

            Timber.d("getOrganizationById() - db write")
            response?.networkToOrganizationsEntities()
                ?.let { scope.launch { runCatching { organizationsDao.insertAll(it) }}}

            Timber.d("getOrganizationById() - end")
        }

    override suspend fun add(item: Organization) = organizationsDao.insert(item.toOrganizationEntity())

    override suspend fun remove(item: Organization) = organizationsDao.delete(item.toOrganizationEntity())

}

