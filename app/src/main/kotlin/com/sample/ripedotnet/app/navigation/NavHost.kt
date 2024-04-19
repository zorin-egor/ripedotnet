package com.sample.ripedotnet.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sample.ripedotnet.app.ui.AppState
import com.sample.ripedotnet.feature.inetnum_by_ip.navigation.INET_NUM_BY_IP_ROUTE
import com.sample.ripedotnet.feature.inetnum_by_ip.navigation.inetNumByIpScreen
import com.sample.ripedotnet.feature.inetnums_by_org_id.navigation.inetNumsByOrgIdScreen
import com.sample.ripedotnet.feature.inetnums_by_org_id.navigation.navigateToInetNumsByOrgId
import com.sample.ripedotnet.feature.organizations_by_name.navigation.ORGANIZATIONS_BY_NAME_ROUTE
import com.sample.ripedotnet.feature.organizations_by_name.navigation.organizationsByNameScreen
import com.sample.ripedotnet.feature.settings.navigation.settingsScreen
import timber.log.Timber

@Composable
fun NavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = ORGANIZATIONS_BY_NAME_ROUTE,
) {
    Timber.d("NavHost($appState)")

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        organizationsByNameScreen(
            onItemClick = {
                Timber.d("onOrganizationByNameClick($it)")
                navController.navigateToInetNumsByOrgId(it) {
                    popUpTo(ORGANIZATIONS_BY_NAME_ROUTE)
                }
            },
            onShowSnackbar = onShowSnackbar
        )
        settingsScreen()
        inetNumByIpScreen(
            onItemClick = {
                Timber.d("onInetNumByIpClick($it)")
                navController.navigateToInetNumsByOrgId(it) {
                    popUpTo(INET_NUM_BY_IP_ROUTE)
                }
            },
            onShowSnackbar = onShowSnackbar
        )
        inetNumsByOrgIdScreen(
            onItemClick = { Timber.d("onInetNumByOrgIdClick($it)") },
            onBackClick = {
                navController.navigateUp()
            },
            onShowSnackbar = onShowSnackbar
        )
    }
}
