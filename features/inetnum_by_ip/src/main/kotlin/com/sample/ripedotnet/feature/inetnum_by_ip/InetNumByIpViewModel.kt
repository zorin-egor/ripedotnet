package com.sample.ripedotnet.feature.inetnum_by_ip

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ripedotnet.core.common.extensions.isIpValid
import com.sample.ripedotnet.core.domain.GetInetNumByIpUseCase
import com.sample.ripedotnet.core.domain.GetSelfIpUseCase
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.exceptions.NetworkException
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

    private fun Flow<InetNum?>.getInetNum(ip: String): Job =
        map {
            InetNumByIpUiState(
                query = ip,
                state = if (it != null) {
                    InetNumByIpUiStates.Success(inetNum = it)
                } else {
                    InetNumByIpUiStates.Empty
                }
            )
        }
        .onEach(_state::emit)
        .catch {
            val error = context.getErrorMessage(it)
            Timber.e(error)

            if (it is NetworkException) {
                _state.emit(InetNumByIpUiState(
                    query = ip,
                    state = InetNumByIpUiStates.Empty
                ))
            } else {
                _action.emit(InetNumByIpActions.ShowError(error))
            }
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
        getSelfIpUseCase()
            .onEach {
                if (it != null) {
                    setQuery(it)
                    queryInetNumByIp(it)
                } else {
                    _state.emit(InetNumByIpUiState(
                        query = "",
                        state = InetNumByIpUiStates.Empty
                    ))
                }
            }
            .launchIn(scope = viewModelScope)
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}