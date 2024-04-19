package com.sample.ripedotnet.core.network.retrofit


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sample.ripedotnet.core.network.BuildConfig
import com.sample.ripedotnet.core.network.NetworkDataSource
import com.sample.ripedotnet.core.network.models.NetworkRipeObject
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.BACKEND_URL
const val SELF_IP_URL = BuildConfig.IP_URL

@Singleton
internal class RetrofitNetwork @Inject constructor(
    okHttpBuilder: OkHttpClient.Builder,
    json: Json
) : NetworkDataSource {

    private var api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpBuilder.addInterceptor(HeadersConfigInterceptor()).build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitApi::class.java)

    override suspend fun getSearchByIp(query: String): Response<NetworkRipeObject> =
        api.getSearchByIp(query = query)

    override suspend fun getSearchByName(query: String, offset: Int?, limit: Int?): Response<NetworkRipeObject> =
        api.getSearchByName(
            query = query,
            offset = offset,
            limit = limit,
        )

    override suspend fun getOrgDetails(orgId: String): Response<NetworkRipeObject> =
        api.getOrgById(orgId = orgId)

    override suspend fun getPersonDetails(personId: String): Response<NetworkRipeObject> =
        api.getPersonById(personId = personId)

    override suspend fun getOrgNetworks(query: String, offset: Int?, limit: Int?): Response<NetworkRipeObject> =
        api.getOrgNetworks(
            query = query,
            offset = offset,
            limit = limit,
        )

    override suspend fun getNetworkByIp(ipRange: String): Response<NetworkRipeObject> =
        api.getNetworkByIp(ipRange = ipRange)

    override suspend fun getUrlRipe(url: String): Response<NetworkRipeObject> =
        api.getUrlRipe(url = url)

    override suspend fun getUrlFree(url: String, headers: Map<String, String>): Response<String> =
        api.getUrlFree(url, headers)
}

