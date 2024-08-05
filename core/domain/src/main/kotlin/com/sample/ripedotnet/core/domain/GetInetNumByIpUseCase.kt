package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.common.extensions.isIpValid
import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepository
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class GetInetNumByIpUseCase @Inject constructor(
    private val inetNumsRepository: InetNumRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(ip: String): Flow<Result<InetNum?>> {
        if (!isIpValid(ip)) {
            return flowOf(Result.failure(IllegalArgumentException("Not valid")))
        }
        return inetNumsRepository.getInetNumByIp(ip = ip)
            .onStart { delay(500) }
            .flowOn(dispatcher)
    }

}
