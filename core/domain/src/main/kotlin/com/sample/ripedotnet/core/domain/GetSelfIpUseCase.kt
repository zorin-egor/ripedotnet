package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepository
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetSelfIpUseCase @Inject constructor(
    private val inetNumRepository: InetNumRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<String?> =
        inetNumRepository.getSelfIp()
            .flowOn(dispatcher)
}
