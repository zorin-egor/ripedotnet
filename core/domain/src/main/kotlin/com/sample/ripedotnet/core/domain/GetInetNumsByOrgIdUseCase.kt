package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepository
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject


class GetInetNumsByOrgIdUseCase @Inject constructor(
    private val inetNumsRepository: InetNumRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    companion object {
        private const val LIMIT = 30
    }

    private val inetNums = ArrayList<InetNum>()
    private val lock = Any()
    private val offset: Int get() = inetNums.size
    private var hasNext = true
    private var previousId = ""

    operator fun invoke(): Flow<Result<List<InetNum>>> {
        Timber.d("invoke($hasNext) - next")

        synchronized(lock) {
            if (!hasNext || previousId.isEmpty()) {
                return flowOf(Result.success(inetNums.toList()))
            }
        }

        return inetNumsRepository.getInetNumsByOrgId(id = previousId, offset = offset, limit = LIMIT)
            .map { new ->
                if (new.isSuccess) {
                    synchronized(lock) {
                        val newItems = new.getOrDefault(emptyList())
                        hasNext = newItems.size >= LIMIT
                        newItems.forEach { item ->
                            if (inetNums.find { it.id == item.id } == null) {
                                inetNums.add(item)
                            }
                        }
                        Result.success(inetNums.toList())
                    }
                } else {
                    new
                }
            }
            .onCompletion {
                Timber.d("invoke($hasNext, $previousId) - onCompletion")
                delay(500)
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(id: String): Flow<Result<List<InetNum>>> {
        Timber.d("invoke($id) - new")

        synchronized(lock) {
            if (id == previousId && inetNums.isNotEmpty()) {
                return flowOf(Result.success(inetNums.toList()))
            }
        }

        return inetNumsRepository.getInetNumsByOrgId(id = id, offset = 0, limit = LIMIT)
            .onStart {
                Timber.d("invoke($id) - onStart")
                delay(500)
            }
            .map { new ->
                if (new.isSuccess) {
                    val items = synchronized(lock) {
                        val newItems = new.getOrDefault(emptyList())
                        previousId = id
                        hasNext = newItems.size >= LIMIT
                        inetNums.clear()
                        inetNums.addAll(newItems)
                        inetNums.toList()
                    }
                    Result.success(items)
                } else {
                    new
                }
            }
            .flowOn(dispatcher)
    }

}
