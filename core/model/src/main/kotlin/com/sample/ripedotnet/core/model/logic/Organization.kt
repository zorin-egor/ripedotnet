package com.sample.ripedotnet.core.model.logic

data class Organization(
    val id: String,
    val name: String,
    val address: String,
    val created: String,
    val country: String?,
    val countryFlagUrl: String?,
    val modified: String?,
    val phone: String?,
    val fax: String?
)