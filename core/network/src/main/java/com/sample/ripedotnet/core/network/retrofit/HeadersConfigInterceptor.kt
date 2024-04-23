package com.sample.ripedotnet.core.network.retrofit

import android.net.Uri
import okhttp3.Interceptor

class HeadersConfigInterceptor : Interceptor {

    companion object {
        private const val HEADER_ACCEPT = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestHost = chain.request().url.host
        val baseHost = Uri.parse(BASE_URL).host
        if (requestHost != baseHost) {
            return chain.proceed(chain.request())
        }

        val newRequest = chain.request().newBuilder()
            .addHeader("Accept", HEADER_ACCEPT)

        return chain.proceed(newRequest.build())
    }

}