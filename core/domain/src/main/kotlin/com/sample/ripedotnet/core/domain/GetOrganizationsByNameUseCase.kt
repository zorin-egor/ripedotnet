package com.sample.ripedotnet.core.domain

import com.sample.ripedotnet.core.data.repositories.organizations.OrganizationsRepository
import com.sample.ripedotnet.core.model.logic.Organization
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
import java.util.TreeSet
import javax.inject.Inject


class GetOrganizationsByNameUseCase @Inject constructor(
    private val organizationsRepository: OrganizationsRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    companion object {
        private const val LIMIT = 30
    }

    private val organizationsSet = TreeSet<Organization> { o1, o2 -> o1.id.compareTo(o2.id) }
    private val mutex = Any()
    private val offset: Int get() = organizationsSet.size
    private var previousName = ""
    private var hasNext = true

    operator fun invoke(): Flow<List<Organization>> {
        Timber.d("invoke($hasNext, $previousName) - next")

        synchronized(mutex) {
            if (!hasNext || previousName.isEmpty()) {
                return flowOf(organizationsSet.toList())
            }
        }

        return organizationsRepository.getOrganizationsByName(name = previousName, offset = offset, limit = LIMIT)
            .map {
                synchronized(mutex) {
                    hasNext = it.size >= LIMIT
                    organizationsSet.addAll(it)
                    organizationsSet.toList()
                }
            }
            .onCompletion {
                Timber.d("invoke($hasNext, $previousName) - onCompletion")
                delay(500)
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(name: String): Flow<List<Organization>> {
        Timber.d("invoke($name, $previousName) - new")

        synchronized(mutex) {
            if (name == previousName && organizationsSet.isNotEmpty()) {
                return flowOf(organizationsSet.toList())
            }
        }

        return organizationsRepository.getOrganizationsByName(name = name, offset = 0, limit = LIMIT)
            .onStart {
                Timber.d("invoke($name, $previousName) - onStart")
                delay(500)
            }
            .map {
                synchronized(mutex) {
                    previousName = name
                    hasNext = it.size >= LIMIT
                    organizationsSet.clear()
                    organizationsSet.addAll(it)
                    organizationsSet.toList()
                }
            }
            .flowOn(dispatcher)
    }

}