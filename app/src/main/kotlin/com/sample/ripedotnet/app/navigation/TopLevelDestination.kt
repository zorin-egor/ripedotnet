package com.sample.ripedotnet.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.sample.ripedotnet.core.designsystem.icon.Icons
import com.sample.ripedotnet.feature.inetnum_by_ip.navigation.INET_NUM_BY_IP_ROUTE
import com.sample.ripedotnet.feature.organizations_by_name.navigation.ORGANIZATIONS_BY_NAME_ROUTE
import com.sample.ripedotnet.feature.settings.navigation.SETTINGS_ROUTE
import com.sample.ripedotnet.feature.inetnum_by_ip.R as InetNumByIpR
import com.sample.ripedotnet.feature.organizations_by_name.R as OrgsByNameR
import com.sample.ripedotnet.feature.settings.R as SettingsR

enum class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
) {
    ORGANIZATIONS(
        route = ORGANIZATIONS_BY_NAME_ROUTE,
        selectedIcon = Icons.Search,
        unselectedIcon = Icons.SearchBorder,
        iconTextId = OrgsByNameR.string.feature_organizations_by_name_title,
    ),
    INET_NUM(
        route = INET_NUM_BY_IP_ROUTE,
        selectedIcon = Icons.Cable,
        unselectedIcon = Icons.CableBorder,
        iconTextId = InetNumByIpR.string.feature_inet_num_by_ip_title,
    ),
    SETTINGS(
        route = SETTINGS_ROUTE,
        selectedIcon = Icons.Settings,
        unselectedIcon = Icons.SettingsBorder,
        iconTextId = SettingsR.string.feature_settings_title,
    ),
}
