package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceHolder(
    @SerialName("key") val key: String,
    @SerialName("name") val name: String,
)