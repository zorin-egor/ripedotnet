package com.sample.ripedotnet.feature.organizations_by_name

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ripedotnet.core.domain.GetOrganizationsByNameUseCase
import com.sample.ripedotnet.core.model.logic.Organization
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
internal class OrganizationsByNameViewModel @Inject constructor(
    private val getOrganizationsByNameUseCase: GetOrganizationsByNameUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<OrganizationsByNameUiState>(OrganizationsByNameUiState(
        query = "",
        state = OrganizationsByNameUiStates.Start
    ))

    val state: StateFlow<OrganizationsByNameUiState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<OrganizationsByNameActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<OrganizationsByNameActions> = _action.asSharedFlow()

    private val previousQuery: String get() = _state.value.query

    private var organizationsJob: Job? = null

    private fun Flow<Result<List<Organization>>>.getOrganizations(query: String): Job =
        map { result ->
            val items = result.getOrNull()
            val exception = result.exceptionOrNull()
            val state = when {
                result.isSuccess && items?.isEmpty() == true -> OrganizationsByNameUiStates.Empty
                result.isSuccess && items?.isNotEmpty() == true -> OrganizationsByNameUiStates.Success(
                    organizations = items,
                    isBottomProgress = false
                )
                else -> {
                    val error = context.getErrorMessage(exception)
                    Timber.e(error)
                    _action.emit(OrganizationsByNameActions.ShowError(error))
                    OrganizationsByNameUiStates.Empty
                }
            }

            OrganizationsByNameUiState(
                query = query,
                state = state
            )
        }
        .onEach(_state::emit)
        .catch {
            Timber.e(it)
            _action.emit(OrganizationsByNameActions.ShowError(context.getErrorMessage(it)))
            setBottomProgress(false)
        }
        .launchIn(scope = viewModelScope)

    fun queryOrganizations(name: String) {
        Timber.d("queryOrganizations($name)")
        organizationsJob?.cancel()

        setQuery(name)

        if (name.trim().isEmpty()) {
            _state.tryEmit(OrganizationsByNameUiState(
                query = "",
                state = OrganizationsByNameUiStates.Start
            ))
            return
        }

        organizationsJob = getOrganizationsByNameUseCase(name = name)
            .onStart {
                Timber.d("queryOrganizations() - onStart")
                _state.emit(OrganizationsByNameUiState(
                    query = name,
                    state = OrganizationsByNameUiStates.Loading
                ))
            }
            .getOrganizations(query = name)
    }

    fun nextOrganizations() {
        if (organizationsJob?.isActive == true) return
        organizationsJob = getOrganizationsByNameUseCase()
            .onStart {
                Timber.d("nextOrganizations() - onStart")
                setBottomProgress(true)
            }
            .getOrganizations(query = previousQuery)
    }

    private suspend fun setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        val prevStates = _state.value.state
        if (prevStates is OrganizationsByNameUiStates.Success) {
            _state.emit(prevState.copy(state = prevStates.copy(isBottomProgress = isBottomProgress)))
        }
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}