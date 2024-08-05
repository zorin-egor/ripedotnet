package com.sample.ripedotnet.feature.inetnums_by_org_id

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ripedotnet.core.domain.GetInetNumsByOrgIdUseCase
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.exceptions.NetworkException
import com.sample.ripedotnet.core.ui.ext.getErrorMessage
import com.sample.ripedotnet.feature.inetnums_by_org_id.navigation.InetNumsByOrgIdArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class InetNumsByOrgIdViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInetNumsByOrgIdUseCase: GetInetNumsByOrgIdUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val inetNumsArgs: InetNumsByOrgIdArgs = InetNumsByOrgIdArgs(savedStateHandle)

    private val orgId = inetNumsArgs.orgId

    private val _state = MutableStateFlow<InetNumsByOrgIdUiState>(InetNumsByOrgIdUiState.Loading)

    val state: StateFlow<InetNumsByOrgIdUiState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<InetNumsByOrgIdActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<InetNumsByOrgIdActions> = _action.asSharedFlow()

    private var inetNumsByOrgIdJob: Job? = null

    init {
        getInetNumsByOrgId(id = orgId)
    }

    private fun Flow<Result<List<InetNum>>>.getInetNumsByOrgId(): Job =
        map {
            val items = it.getOrNull()
            val error = it.exceptionOrNull()
            when {
                it.isSuccess && items?.isNotEmpty() == true -> InetNumsByOrgIdUiState.Success(
                    inetNums = items,
                    isBottomProgress = false
                )
                it.isSuccess && items?.isNotEmpty() == false -> InetNumsByOrgIdUiState.Empty
                it.isFailure && error != null -> throw error
                else -> throw IllegalStateException("Unknown state")
            }
        }
        .onEach(_state::emit)
        .catch {
            val error = context.getErrorMessage(it)
            Timber.e(error)

            if (it is NetworkException) {
                _state.emit(InetNumsByOrgIdUiState.Empty)
            } else {
                _action.emit(InetNumsByOrgIdActions.ShowError(error))
            }

            setBottomProgress(false)
        }
        .launchIn(scope = viewModelScope)

    private fun getInetNumsByOrgId(id: String) {
        Timber.d("getInetNumsByOrgId($id)")
        inetNumsByOrgIdJob?.cancel()

        if (id.trim().isEmpty()) {
            _state.tryEmit(InetNumsByOrgIdUiState.Empty)
            return
        }

        inetNumsByOrgIdJob = getInetNumsByOrgIdUseCase(id = id)
            .onStart {
                Timber.d("getInetNumsByOrgId() - onStart")
                _state.emit(InetNumsByOrgIdUiState.Loading)
            }
            .getInetNumsByOrgId()
    }

    fun nextInetNumsByOrgId() {
        if (inetNumsByOrgIdJob?.isActive == true) return
        inetNumsByOrgIdJob = getInetNumsByOrgIdUseCase()
            .onStart {
                Timber.d("nextInetNumsByOrgId() - onStart")
                setBottomProgress(true)
            }
            .getInetNumsByOrgId()
    }

    private suspend fun setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        if (prevState is InetNumsByOrgIdUiState.Success) {
            _state.emit(prevState.copy(isBottomProgress = isBottomProgress))
        }
    }

}