package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Object(
    @SerialName("attributes") val attributes: Attributes,
    @SerialName("link") val link: LinkX,
    @SerialName("primary-key") val primaryKey: PrimaryKey,
    @SerialName("source") val source: Source,
    @SerialName("type") val type: String,
    @SerialName("resource-holder") val resourceHolder: ResourceHolder?
)