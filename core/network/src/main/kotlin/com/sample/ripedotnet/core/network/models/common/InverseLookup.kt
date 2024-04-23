package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InverseLookup(
    @SerialName("inverse-attribute") val inverseAttribute: List<InverseAttribute>?
)