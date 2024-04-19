package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkX(
    @SerialName("href") val href: String,
    @SerialName("type") val type: String
)