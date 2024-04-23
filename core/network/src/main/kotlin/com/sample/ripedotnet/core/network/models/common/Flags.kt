package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Flags(
    @SerialName("flag") val flag: List<Flag>
)