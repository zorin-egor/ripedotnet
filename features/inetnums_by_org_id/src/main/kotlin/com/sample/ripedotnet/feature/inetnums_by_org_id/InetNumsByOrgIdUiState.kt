package com.sample.ripedotnet.feature.inetnums_by_org_id

import com.sample.ripedotnet.core.model.logic.InetNum

internal sealed interface InetNumsByOrgIdUiState {
    data class Success(
        val inetNums: List<InetNum>,
        val isBottomProgress: Boolean
    ) : InetNumsByOrgIdUiState
    data object Loading : InetNumsByOrgIdUiState
    data object Empty : InetNumsByOrgIdUiState
}