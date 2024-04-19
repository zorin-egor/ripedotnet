package com.sample.ripedotnet.feature.inetnums_by_org_id


internal sealed interface InetNumsByOrgIdActions {
    data class ShowError(val error: String) : InetNumsByOrgIdActions
}