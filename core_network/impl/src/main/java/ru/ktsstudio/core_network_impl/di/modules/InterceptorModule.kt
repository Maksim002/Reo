package ru.ktsstudio.core_network_impl.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import ru.ktsstudio.common.data.AppVersion
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.account.AuthDelegate
import ru.ktsstudio.core_network_api.RetrofitExceptionParser
import ru.ktsstudio.core_network_api.qualifiers.AuthApiUrl
import ru.ktsstudio.core_network_api.qualifiers.ClientId
import ru.ktsstudio.core_network_api.qualifiers.ClientSecret
import ru.ktsstudio.core_network_impl.di.qualifiers.AuthFailedInterceptor
import ru.ktsstudio.core_network_impl.di.qualifiers.AuthHeaderInterceptor
import ru.ktsstudio.core_network_impl.di.qualifiers.DefaultLoggingInterceptor
import ru.ktsstudio.core_network_impl.di.qualifiers.ErrorInterceptor
import ru.ktsstudio.core_network_impl.di.qualifiers.HeadersLoggingInterceptor
import ru.ktsstudio.core_network_impl.di.qualifiers.VersionInterceptor
import ru.ktsstudio.core_network_impl.utils.network.interceptors.AppVersionInterceptor
import ru.ktsstudio.core_network_impl.utils.network.interceptors.AuthorizationFailedInterceptor
import ru.ktsstudio.core_network_impl.utils.network.interceptors.AuthorizationHeaderInterceptor
import ru.ktsstudio.core_network_impl.utils.network.interceptors.ServerErrorInterceptor
import timber.log.Timber

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2019.
 */
@Module
object InterceptorModule {

    private const val LOG_TAG = "OkHttp"

    @Provides
    @DefaultLoggingInterceptor
    fun providerHttpDefaultLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message -> Timber.tag(LOG_TAG).d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @ErrorInterceptor
    fun provideErrorInterceptor(
        exceptionParser: RetrofitExceptionParser
    ): Interceptor {
        return ServerErrorInterceptor(exceptionParser)
    }

    @Provides
    @HeadersLoggingInterceptor
    fun providerHttpHeadersLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message -> Timber.tag(LOG_TAG).d(message) }
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    @Provides
    @VersionInterceptor
    fun provideAppVersionInterceptor(@AppVersion appVersion: String): Interceptor {
        return AppVersionInterceptor(appVersion)
    }

    @Provides
    @AuthHeaderInterceptor
    fun provideAuthHeaderInterceptor(accountManager: AccountManager): Interceptor {
        return AuthorizationHeaderInterceptor(accountManager)
    }

    @Provides
    @AuthFailedInterceptor
    fun providesAuthFailedInterceptor(
        accountManager: AccountManager,
        authDelegate: AuthDelegate,
        @AuthApiUrl authApiUrl: String,
        @ClientId clientId: String,
        @ClientSecret clientSecret: String,
        gson: Gson
    ): Interceptor {
        return AuthorizationFailedInterceptor(
            accountManager = accountManager,
            authDelegate = authDelegate,
            authApiUrl = authApiUrl,
            clientId = clientId,
            clientSecret = clientSecret,
            gson = gson
        )
    }
}
