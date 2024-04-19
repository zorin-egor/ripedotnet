package com.sample.ripedotnet.feature.inetnum_by_ip


internal sealed interface InetNumByIpActions {
    data class ShowError(val error: String) : InetNumByIpActions
}