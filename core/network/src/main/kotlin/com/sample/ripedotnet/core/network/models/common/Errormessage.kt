package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Errormessage(
    @SerialName("severity") val severity: String,
    @SerialName("text") val text: String
)