package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeFilter(
    @SerialName("id") val id: String
)