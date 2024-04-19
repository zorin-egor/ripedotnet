package com.sample.ripedotnet.core.data.repositories.netnum

import com.sample.ripedotnet.core.model.logic.InetNum
import kotlinx.coroutines.flow.Flow

interface InetNumRepository {

    fun getInetNumByIp(ip: String): Flow<InetNum?>

    fun getInetNumsByOrgId(id: String, offset: Int, limit: Int = 30): Flow<List<InetNum>>

    fun getSelfIp(): Flow<String?>

}