package com.sample.ripedotnet.core.data.di

import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepository
import com.sample.ripedotnet.core.data.repositories.netnum.InetNumRepositoryImpl
import com.sample.ripedotnet.core.data.repositories.organizations.OrganizationsRepository
import com.sample.ripedotnet.core.data.repositories.organizations.OrganizationsRepositoryImpl
import com.sample.ripedotnet.core.data.repositories.settings.SettingsDataRepository
import com.sample.ripedotnet.core.data.repositories.settings.SettingsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun bindsUsersRepository(
        usersRepository: OrganizationsRepositoryImpl,
    ): OrganizationsRepository

    @Binds
    internal abstract fun bindsDetailsRepository(
        detailsRepository: InetNumRepositoryImpl,
    ): InetNumRepository

    @Binds
    internal abstract fun bindsSettingsDataRepository(
        settingsDataRepository: SettingsDataRepositoryImpl,
    ): SettingsDataRepository

}
