package com.sample.ripedotnet.core.data.repositories.settings

import com.sample.ripedotnet.core.model.ui.DarkThemeConfig
import com.sample.ripedotnet.core.model.ui.SettingsData
import com.sample.ripedotnet.core.model.ui.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsDataRepository {

    val settingsData: Flow<SettingsData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

}
