package com.sample.ripedotnet.core.database.di

import com.sample.ripedotnet.core.database.Database
import com.sample.ripedotnet.core.database.dao.InetNumsDao
import com.sample.ripedotnet.core.database.dao.OrganizationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesUsersDao(
        database: Database,
    ): InetNumsDao = database.usersDao()

    @Provides
    fun providesDetailsDao(
        database: Database,
    ): OrganizationsDao = database.detailsDao()
}
