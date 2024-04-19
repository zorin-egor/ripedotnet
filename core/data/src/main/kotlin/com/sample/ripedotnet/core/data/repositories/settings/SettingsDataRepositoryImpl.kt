package com.sample.ripedotnet.core.data.repositories.settings

import com.sample.ripedotnet.core.datastore.SettingsDataStoreProto
import com.sample.ripedotnet.core.model.ui.DarkThemeConfig
import com.sample.ripedotnet.core.model.ui.SettingsData
import com.sample.ripedotnet.core.model.ui.ThemeBrand
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SettingsDataRepositoryImpl @Inject constructor(
    private val dataSourceProto: SettingsDataStoreProto,
) : SettingsDataRepository {

    override val settingsData: Flow<SettingsData> =
        dataSourceProto.settingsData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        dataSourceProto.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataSourceProto.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        dataSourceProto.setDynamicColorPreference(useDynamicColor)
    }

}