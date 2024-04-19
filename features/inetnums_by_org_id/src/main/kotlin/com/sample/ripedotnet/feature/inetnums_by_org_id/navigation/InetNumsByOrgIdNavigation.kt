package com.sample.ripedotnet.feature.inetnums_by_org_id.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sample.ripedotnet.feature.inetnums_by_org_id.InetNumsByOrgIdScreen

const val INET_NUMS_ARG_ORG_ID = "inet_nums_org_id"
const val INET_NUMS_BY_ORG_ID_ROUTE_BASE = "inet_nums_by_org_id_route"
const val INET_NUMS_BY_ORG_ID_ROUTE = "$INET_NUMS_BY_ORG_ID_ROUTE_BASE?$INET_NUMS_ARG_ORG_ID={$INET_NUMS_ARG_ORG_ID}"

internal class InetNumsByOrgIdArgs(val orgId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle.get<String>(INET_NUMS_ARG_ORG_ID)))
}

fun NavController.navigateToInetNumsByOrgId(orgId: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    val route = "${INET_NUMS_BY_ORG_ID_ROUTE_BASE}?${INET_NUMS_ARG_ORG_ID}=$orgId"
    navigate(route) {
        navOptions()
    }
}

fun NavGraphBuilder.inetNumsByOrgIdScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = INET_NUMS_BY_ORG_ID_ROUTE,
        arguments = listOf(
            navArgument(INET_NUMS_ARG_ORG_ID) {
                nullable = false
                type = NavType.StringType
            },
        ),
    ) {
        InetNumsByOrgIdScreen(
            onOrganizationClick = onItemClick,
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}