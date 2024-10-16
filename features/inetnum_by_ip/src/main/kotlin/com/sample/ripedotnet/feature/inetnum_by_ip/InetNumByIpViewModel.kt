package com.sample.ripedotnet.feature.inetnum_by_ip

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ripedotnet.core.common.extensions.isIpValid
import com.sample.ripedotnet.core.domain.GetInetNumByIpUseCase
import com.sample.ripedotnet.core.domain.GetSelfIpUseCase
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.ui.ext.getErrorMessage
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class InetNumByIpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInetNumByIpUseCase: GetInetNumByIpUseCase,
    private val getSelfIpUseCase: GetSelfIpUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _action = MutableSharedFlow<InetNumByIpActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<InetNumByIpActions> = _action.asSharedFlow()

    private val _state = MutableStateFlow<InetNumByIpUiState>(InetNumByIpUiState(
        query = "",
        state = InetNumByIpUiStates.Start
    ))

    val state: StateFlow<InetNumByIpUiState> = _state.asStateFlow()

    private var inetNumJob: Job? = null
    private var selfIpJob: Job? = null

    private fun Flow<Result<InetNum?>>.getInetNum(ip: String): Job =
        map { result ->
            val item = result.getOrNull()
            val exception = result.exceptionOrNull()
            val state = when {
                result.isSuccess && item != null -> InetNumByIpUiStates.Success(inetNum = item)
                result.isSuccess && item == null -> InetNumByIpUiStates.Empty
                else -> {
                    val error = context.getErrorMessage(exception)
                    Timber.e(error)
                    _action.emit(InetNumByIpActions.ShowError(error))
                    InetNumByIpUiStates.Empty
                }
            }

            InetNumByIpUiState(
                query = ip,
                state = state
            )
        }
        .onEach(_state::emit)
        .catch {
            Timber.e(t = it, message = "catch")
            _action.emit(InetNumByIpActions.ShowError(context.getErrorMessage(it)))
        }
        .launchIn(scope = viewModelScope)

    fun queryInetNumByIp(ip: String) {
        Timber.d("queryInetNumByIp($ip)")
        inetNumJob?.cancel()

        setQuery(ip)

        when {
            ip.isEmpty() -> {
                _state.tryEmit(InetNumByIpUiState(
                    query = "",
                    state = InetNumByIpUiStates.Start
                ))
                return
            }
            !isIpValid(ip) -> {
                _state.tryEmit(InetNumByIpUiState(
                    query = ip,
                    state = InetNumByIpUiStates.Start
                ))
                return
            }
        }

        inetNumJob = getInetNumByIpUseCase(ip = ip)
            .onStart {
                Timber.d("queryInetNumByIp() - onStart")
                _state.emit(
                    InetNumByIpUiState(
                        query = ip,
                        state = InetNumByIpUiStates.Loading
                    )
                )
            }
            .getInetNum(ip = ip)
    }

    fun getSelfIp() {
        selfIpJob?.cancel()
        selfIpJob = viewModelScope.launch {
            val ipResult = getSelfIpUseCase()
            if (ipResult != null) {
                setQuery(ipResult)
                queryInetNumByIp(ipResult)
            } else {
                _state.emit(InetNumByIpUiState(
                    query = "",
                    state = InetNumByIpUiStates.Empty
                ))
            }
        }
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}