package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryStrings(
    @SerialName("query-string") val queryString: List<QueryString>
)