package com.sample.ripedotnet.core.network.models.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Version(
    @SerialName("commit-id") val commitId: String,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("version") val version: String
)