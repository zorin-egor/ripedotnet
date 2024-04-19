package com.sample.ripedotnet.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.ripedotnet.core.data.repositories.settings.SettingsDataRepository
import com.sample.ripedotnet.core.model.ui.SettingsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    settingsDataRepository: SettingsDataRepository
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = settingsDataRepository.settingsData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val settingsData: SettingsData) : MainActivityUiState
}
