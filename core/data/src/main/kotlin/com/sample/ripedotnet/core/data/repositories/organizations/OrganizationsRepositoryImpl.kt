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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class OrganizationsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val organizationsDao: OrganizationsDao,
    @IoScope private val scope: CoroutineScope
) : OrganizationsRepository {

    override fun getOrganizationsByName(name: String, offset: Int, limit: Int): Flow<Result<List<Organization>>> =
        flow<Result<List<Organization>>> {
            Timber.d("getOrganizationsByName($name, $offset, $limit)")

            Timber.d("getOrganizationsByName() - db")
            kotlin.runCatching {
                organizationsDao.getOrganizations(query = name, offset = offset, limit = limit)
            }.onFailure(Timber::e)
                .getOrNull()?.takeIf { it.isNotEmpty() }
                    ?.let { emit(Result.success(it.toOrgModels())) }

            Timber.d("getOrganizationsByName() - network request")
            val response = networkDatasource.getSearchByName(query = name, offset = offset, limit = limit)
                .getResultOrThrow()

            emit(Result.success(response?.networkToOrganizationModel() ?: emptyList()))

            Timber.d("getOrganizationsByName() - db write")
            response?.networkToOrganizationsEntities()?.let {
                scope.launch {
                    runCatching { organizationsDao.insertAll(it) }
                        .onFailure(Timber::e)
                }
            }

            Timber.d("getOrganizationsByName() - end")
        }.catch { emit(Result.failure<List<Organization>>(it)) }
            .distinctUntilChanged()

    override fun getOrganizationById(id: String): Flow<Result<Organization?>> =
        flow<Result<Organization?>> {
            Timber.d("getOrganizationById($id)")

            Timber.d("getOrganizationById() - db")
            kotlin.runCatching { organizationsDao.getOrganizationById(id = id) }
                .onFailure(Timber::e)
                .getOrNull()?.let { emit(Result.success(it.toOrgModel())) }

            Timber.d("getOrganizationById() - network request")
            val response = networkDatasource.getOrgDetails(orgId = id).getResultOrThrow()

            emit(Result.success(response?.networkToOrganizationModel()?.firstOrNull()))

            Timber.d("getOrganizationById() - db write")
            response?.networkToOrganizationsEntities()?.let {
                scope.launch {
                    runCatching { organizationsDao.insertAll(it) }
                        .onFailure(Timber::e)
                }
            }

            Timber.d("getOrganizationById() - end")
        }.catch { emit(Result.failure(it)) }
            .distinctUntilChanged()

    override suspend fun add(item: Organization) = organizationsDao.insert(item.toOrganizationEntity())

    override suspend fun remove(item: Organization) = organizationsDao.delete(item.toOrganizationEntity())

}

