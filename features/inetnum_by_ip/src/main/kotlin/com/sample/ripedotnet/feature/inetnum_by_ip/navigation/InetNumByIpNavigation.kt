package com.sample.ripedotnet.feature.inetnum_by_ip.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sample.ripedotnet.feature.inetnum_by_ip.InetNumByIpScreen

const val INET_NUM_BY_IP_ROUTE = "inet_num_by_ip_route"

fun NavController.navigateToInetNumByIp(navOptions: NavOptions) {
    navigate(INET_NUM_BY_IP_ROUTE, navOptions)
}

fun NavGraphBuilder.inetNumByIpScreen(
    onItemClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = INET_NUM_BY_IP_ROUTE,
    ) {
        InetNumByIpScreen(onItemClick = onItemClick, onShowSnackbar = onShowSnackbar)
    }
}