package com.sample.ripedotnet.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.sample.ripedotnet.core.datastore.DataStorePreference
import com.sample.ripedotnet.core.datastore.DataStoreProto
import com.sample.ripedotnet.core.datastore.DataStoreProtoSerializer
import com.sample.ripedotnet.core.datastore.SettingsDataStore
import com.sample.ripedotnet.core.datastore.SettingsDataStoreProto
import com.sample.ripedotnet.core.datastore.SettingsPreference
import com.sample.ripedotnet.core.network.Dispatcher
import com.sample.ripedotnet.core.network.Dispatchers
import com.sample.ripedotnet.core.network.di.IoScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(Dispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @IoScope scope: CoroutineScope,
        userPreferencesSerializer: DataStoreProtoSerializer,
    ): DataStore<SettingsDataStore> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            migrations = emptyList(),
        ) {
            context.dataStoreFile("settings_data_store.pb")
        }

    @Provides
    @Singleton
    fun providesSettingsPreference(
        @ApplicationContext context: Context
    ): SettingsPreference = DataStorePreference(context)

    @Provides
    @Singleton
    fun providesSettingsDataStore(
        dataStore: DataStore<SettingsDataStore>
    ): SettingsDataStoreProto = DataStoreProto(dataStore)

}
