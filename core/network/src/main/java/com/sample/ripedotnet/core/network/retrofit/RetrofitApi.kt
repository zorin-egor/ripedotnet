package com.sample.ripedotnet.core.network.retrofit

import com.sample.ripedotnet.core.network.models.NetworkRipeObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    @GET("search?source=ripe&type-filter=inetnum&flags=no-referenced&resource-holder=true")
    suspend fun getSearchByIp(@Query("query-string") query: String): Response<NetworkRipeObject>

    @GET("search?source=ripe&type-filter=organisation&flags=no-referenced")
    suspend fun getSearchByName(
        @Query("query-string") query: String,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<NetworkRipeObject>

    @GET("search?source=ripe&type-filter=inetnum&inverse-attribute=org&flags=no-referenced")
    suspend fun getOrgNetworks(
        @Query("query-string") query: String,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<NetworkRipeObject>

    @GET("ripe/organisation/{orgId}")
    suspend fun getOrgById(@Path("orgId") orgId: String): Response<NetworkRipeObject>

    @GET("ripe/person/{personId}")
    suspend fun getPersonById(@Path("personId") personId: String): Response<NetworkRipeObject>

    @GET("ripe/inetnum/{ipRange}")
    suspend fun getNetworkByIp(@Path("ipRange") ipRange: String): Response<NetworkRipeObject>

    @GET
    suspend fun getUrlRipe(@Url url: String): Response<NetworkRipeObject>

    @GET
    suspend fun getUrlFree(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

}