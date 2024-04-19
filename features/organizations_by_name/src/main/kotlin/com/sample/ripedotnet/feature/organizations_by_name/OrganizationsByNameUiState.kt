package com.sample.ripedotnet.feature.organizations_by_name

import com.sample.ripedotnet.core.model.logic.Organization

internal data class OrganizationsByNameUiState(
    val query: String,
    val state: OrganizationsByNameUiStates
)

internal sealed interface OrganizationsByNameUiStates {
    data class Success(
        val organizations: List<Organization>,
        val isBottomProgress: Boolean
    ) : OrganizationsByNameUiStates
    data object Loading : OrganizationsByNameUiStates
    data object Empty : OrganizationsByNameUiStates
    data object Start: OrganizationsByNameUiStates
}