package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Objects(
    @SerialName("object") val objectX: List<Object>
)