package com.sample.ripedotnet.core.network.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parameters(
    @SerialName("flags") val flags: Flags,
    @SerialName("query-strings") val queryStrings: QueryStrings,
    @SerialName("sources") val sources: Sources,
    @SerialName("type-filters") val typeFilters: TypeFilters,
    @SerialName("inverse-lookup") val inverseLookup: InverseLookup?,
)