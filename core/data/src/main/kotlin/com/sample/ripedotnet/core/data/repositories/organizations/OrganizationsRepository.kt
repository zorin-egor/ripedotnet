package com.sample.ripedotnet.core.data.repositories.organizations

import com.sample.ripedotnet.core.model.logic.Organization
import kotlinx.coroutines.flow.Flow

interface OrganizationsRepository {

    fun getOrganizationsByName(name: String, offset: Int, limit: Int = 30): Flow<List<Organization>>

    fun getOrganizationById(id: String): Flow<Organization?>

    suspend fun add(item: Organization)

    suspend fun remove(item: Organization)

}