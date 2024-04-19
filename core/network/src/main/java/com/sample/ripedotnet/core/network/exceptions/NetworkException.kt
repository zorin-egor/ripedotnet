package com.sample.ripedotnet.core.network.exceptions

open class NetworkException(
    val errorCode: Int,
    private val errorDesc: String? = null
) : Exception(errorDesc)
