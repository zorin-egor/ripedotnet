package com.sample.ripedotnet.core.model.logic

data class InetNum(
    val id: String,
    val ip: String,
    val name: String,
    val orgId: String?,
    val country: String?,
    val countryFlagUrl: String?,
    val descr: String?,
    val ipRange: Pair<String, String?>?,
)