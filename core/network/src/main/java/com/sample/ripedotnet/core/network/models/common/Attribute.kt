package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attribute(
    @SerialName("name") val name: String,
    @SerialName("value") val value: String,
    @SerialName("comment") val comment: String? = null,
    @SerialName("link") val link: LinkX? = null,
    @SerialName("referenced-type") val referencedType: String? = null
)