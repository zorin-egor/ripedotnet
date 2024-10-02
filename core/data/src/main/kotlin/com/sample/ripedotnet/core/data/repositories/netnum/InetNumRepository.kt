package com.sample.ripedotnet.core.data.repositories.netnum

import com.sample.ripedotnet.core.model.logic.InetNum
import kotlinx.coroutines.flow.Flow

interface InetNumRepository {

    fun getInetNumByIp(ip: String): Flow<Result<InetNum?>>

    fun getInetNumsByOrgId(id: String, offset: Int, limit: Int = 30): Flow<Result<List<InetNum>>>

    suspend fun getSelfIp(): String?

}