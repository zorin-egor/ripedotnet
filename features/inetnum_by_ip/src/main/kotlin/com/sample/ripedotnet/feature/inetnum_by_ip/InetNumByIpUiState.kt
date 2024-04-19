package com.sample.ripedotnet.feature.inetnum_by_ip

import com.sample.ripedotnet.core.model.logic.InetNum

internal data class InetNumByIpUiState(
    val query: String,
    val state: InetNumByIpUiStates
)

internal sealed interface InetNumByIpUiStates {
    data class Success(val inetNum: InetNum) : InetNumByIpUiStates
    data object Loading : InetNumByIpUiStates
    data object Empty : InetNumByIpUiStates
    data object Start : InetNumByIpUiStates
}