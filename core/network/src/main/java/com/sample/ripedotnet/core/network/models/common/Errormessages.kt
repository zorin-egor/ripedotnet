package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Errormessages(
    @SerialName("errormessage") val errormessage: List<Errormessage>
)