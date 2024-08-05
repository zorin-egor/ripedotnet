package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.data.repositories.organizations.OrganizationsRepository
import com.sample.ripedotnet.core.model.logic.Organization
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetOrganizationByIdUseCase @Inject constructor(
    private val organizationsRepository: OrganizationsRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(id: String): Flow<Result<Organization?>> =
        organizationsRepository.getOrganizationById(id = id)
            .flowOn(dispatcher)
}
