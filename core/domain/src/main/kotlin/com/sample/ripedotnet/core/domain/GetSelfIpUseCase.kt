package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepository
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetSelfIpUseCase @Inject constructor(
    private val inetNumRepository: InetNumRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): String? =
        withContext(dispatcher) { inetNumRepository.getSelfIp() }
}
