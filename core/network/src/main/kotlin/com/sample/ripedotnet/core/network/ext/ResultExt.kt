package com.sample.ripedotnet.core.network.ext

import com.sample.ripedotnet.core.network.exceptions.NetworkException
import com.sample.ripedotnet.core.network.models.NetworkError
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

fun ResponseBody.getError(): String? {
    val errorStr = runCatching { string() }.getOrNull()
    return errorStr?.jsonToObject<NetworkError>()?.errormessages
        ?.errormessage?.joinToString(separator = " ") { it.text }
        ?: errorStr
}

fun <T> Response<T>.getResultOrThrow(): T? {
    return when(val code = code()) {
        HttpURLConnection.HTTP_OK -> body()
        else -> throw NetworkException(errorCode = code, errorDesc = errorBody()?.getError())
    }
}