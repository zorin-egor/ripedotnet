package com.sample.ripedotnet.feature.organizations_by_name


internal sealed interface OrganizationsByNameActions {
    data class ShowError(val error: String) : OrganizationsByNameActions
}