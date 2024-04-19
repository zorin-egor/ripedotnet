package com.sample.ripedotnet.core.network.models


import com.sample.ripedotnet.core.network.models.common.Objects
import com.sample.ripedotnet.core.network.models.common.Parameters
import com.sample.ripedotnet.core.network.models.common.Service
import com.sample.ripedotnet.core.network.models.common.TermsAndConditions
import com.sample.ripedotnet.core.network.models.common.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRipeObject(
    @SerialName("objects") val objects: Objects,
    @SerialName("terms-and-conditions") val termsAndConditions: TermsAndConditions,
    @SerialName("version") val version: Version,
    @SerialName("parameters") val parameters: Parameters?,
    @SerialName("service") val service: Service?
)