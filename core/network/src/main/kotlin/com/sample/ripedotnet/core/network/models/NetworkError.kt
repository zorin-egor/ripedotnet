package com.sample.ripedotnet.core.network.models

import com.sample.ripedotnet.core.network.models.common.Errormessages
import com.sample.ripedotnet.core.network.models.common.LinkX
import com.sample.ripedotnet.core.network.models.common.TermsAndConditions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkError(
    @SerialName("errormessages") val errormessages: Errormessages,
    @SerialName("link") val link: LinkX? = null,
    @SerialName("terms-and-conditions") val termsAndConditions: TermsAndConditions? = null
)