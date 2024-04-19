package com.sample.ripedotnet.core.database.di

import android.content.Context
import androidx.room.Room
import com.sample.ripedotnet.core.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "app-database",
    ).build()
}
