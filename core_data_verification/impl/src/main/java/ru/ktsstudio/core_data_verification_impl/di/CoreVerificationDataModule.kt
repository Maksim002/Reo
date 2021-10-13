package ru.ktsstudio.core_data_verification_impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.RegisterRepository
import ru.ktsstudio.core_data_verfication_api.data.WasteManagementTypeRepository
import ru.ktsstudio.core_data_verfication_api.data.SyncRepository
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.data.media.FileManagerImpl
import ru.ktsstudio.core_data_verfication_api.data.media.MediaRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.ObjectRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.RegisterRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.SettingsRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.WasteManagementTypeRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.SyncRepositoryImpl
import ru.ktsstudio.core_data_verification_impl.data.network.FileNetworkApi
import ru.ktsstudio.core_data_verification_impl.data.network.RegisterNetworkApi
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit

@Module
interface CoreVerificationDataModule {

    @Binds
    fun bindRegisterRepository(impl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    fun bindWasteManagementTypeRepository(impl: WasteManagementTypeRepositoryImpl): WasteManagementTypeRepository

    @Binds
    fun bindSettingsRepository(repo: SettingsRepositoryImpl): SettingsRepository

    @Binds
    fun bindObjectRepository(impl: ObjectRepositoryImpl): ObjectRepository

    @Binds
    fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository

    @Binds
    fun bindFileManager(impl: FileManagerImpl): FileManager

    @Binds
    fun bindMediaRepository(repo: MediaRepositoryImpl): MediaRepository

    companion object {

        @Provides
        @FeatureScope
        fun provideRegisterNetworkService(@DefaultRetrofit retrofit: Retrofit): RegisterNetworkApi =
            retrofit.create()

        @Provides
        @FeatureScope
        fun provideFileNetworkService(@MediaRetrofit retrofit: Retrofit): FileNetworkApi =
            retrofit.create()
    }
}