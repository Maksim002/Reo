package ru.ktsstudio.feature_auth.di.core

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.data.network.IdentityApi
import ru.ktsstudio.core_network_api.qualifiers.AuthRetrofit
import ru.ktsstudio.feature_auth.data.AuthRepositoryImpl

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.09.2020.
 */

@Module
internal interface CoreAuthModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @Provides
        fun providesAuthService(@AuthRetrofit retrofit: Retrofit): IdentityApi {
            return retrofit.create()
        }
    }

}