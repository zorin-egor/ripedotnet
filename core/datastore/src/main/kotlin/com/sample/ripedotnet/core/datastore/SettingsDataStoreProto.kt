package com.sample.ripedotnet.core.datastore

import com.sample.ripedotnet.core.model.ui.DarkThemeConfig
import com.sample.ripedotnet.core.model.ui.SettingsData
import com.sample.ripedotnet.core.model.ui.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreProto {

    val settingsData: Flow<SettingsData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}