package com.sample.ripedotnet.feature.organizations_by_name.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sample.ripedotnet.feature.organizations_by_name.OrganizationsByNameScreen


const val ORGANIZATIONS_BY_NAME_ROUTE = "organizations_by_name_route"

fun NavController.navigateToOrganizationsByName(navOptions: NavOptions) {
    navigate(ORGANIZATIONS_BY_NAME_ROUTE, navOptions)
}

fun NavGraphBuilder.organizationsByNameScreen(
    onItemClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = ORGANIZATIONS_BY_NAME_ROUTE,
    ) {
        OrganizationsByNameScreen(onOrganizationClick = onItemClick, onShowSnackbar = onShowSnackbar )
    }
}