package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeFilters(
    @SerialName("type-filter") val typeFilter: List<TypeFilter>
)