package com.sample.ripedotnet.core.network

import com.sample.ripedotnet.core.network.models.NetworkRipeObject
import retrofit2.Response

interface NetworkDataSource {

    suspend fun getSearchByIp(query: String): Response<NetworkRipeObject>

    suspend fun getSearchByName(query: String, offset: Int?, limit: Int?): Response<NetworkRipeObject>

    suspend fun getOrgDetails(orgId: String): Response<NetworkRipeObject>

    suspend fun getPersonDetails(personId: String): Response<NetworkRipeObject>

    suspend fun getOrgNetworks(query: String, offset: Int?, limit: Int?): Response<NetworkRipeObject>

    suspend fun getNetworkByIp(ipRange: String): Response<NetworkRipeObject>

    suspend fun getUrlRipe(url: String): Response<NetworkRipeObject>

    suspend fun getUrlFree(url: String, headers: Map<String, String>): Response<String>

}