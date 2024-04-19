package com.sample.ripedotnet.core.network.retrofit

import okhttp3.Interceptor

class HeadersConfigInterceptor : Interceptor {

    companion object {
        private const val HEADER_ACCEPT = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Accept", HEADER_ACCEPT)
        return chain.proceed(newRequest.build())
    }

}