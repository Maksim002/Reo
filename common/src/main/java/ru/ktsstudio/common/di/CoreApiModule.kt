package ru.ktsstudio.common.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.app.ActivityProvider
import ru.ktsstudio.common.app.ActivityProviderImpl
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.account.AccountManagerImpl
import ru.ktsstudio.common.data.account.AuthDelegate
import ru.ktsstudio.common.data.account.AuthDelegateImpl
import ru.ktsstudio.common.data.error.CompositeErrorReporter
import ru.ktsstudio.common.data.error.ErrorReporter
import ru.ktsstudio.common.data.error.FirebaseErrorReporter
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.network.state.NetworkStateProvider
import ru.ktsstudio.common.data.network.state.NetworkStateProviderImpl
import ru.ktsstudio.common.data.settings.SettingsNetworkMapper
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.resource_manager.ResourceManagerImpl
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.id.IdGeneratorImpl
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.AppSchedulerProvider
import ru.ktsstudio.common.utils.rx.SchedulerProvider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */

@Module
abstract class CoreApiModule {

    @Binds
    @FeatureScope
    abstract fun bindActivityProvider(impl: ActivityProviderImpl): ActivityProvider

    @Binds
    @FeatureScope
    abstract fun bindActivityCallbacks(impl: ActivityProviderImpl): Application.ActivityLifecycleCallbacks

    @Binds
    internal abstract fun bindAccountManager(impl: AccountManagerImpl): AccountManager

    @Binds
    internal abstract fun bindAuthDelegate(impl: AuthDelegateImpl): AuthDelegate

    @Binds
    internal abstract fun bindSchedulerProvider(impl: AppSchedulerProvider): SchedulerProvider

    @Binds
    internal abstract fun bindNetworkStateProvider(impl: NetworkStateProviderImpl): NetworkStateProvider

    @Binds
    internal abstract fun bindSettingsNetworkMapper(impl: SettingsNetworkMapper): Mapper<RemoteSettings, Settings>

    @Binds
    internal abstract fun bindIdGenerator(impl: IdGeneratorImpl): IdGenerator

    companion object {

        @Provides
        @FeatureScope
        fun provideResourceManager(context: Context): ResourceManager {
            return ResourceManagerImpl(context = context, cacheSize = 50)
        }

        @Provides
        @FeatureScope
        fun provideActivityProviderImpl() = ActivityProviderImpl()

        @Provides
        @FeatureScope
        fun provideSharedPreferences(context: Context): SharedPreferences {
            val prefsName = "reo_prefs"
            return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        }

        @Provides
        fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences {
            return RxSharedPreferences.create(sharedPreferences)
        }

        @Provides
        fun provideErrorReporter(): ErrorReporter {
            return CompositeErrorReporter(
                reporterList = listOf(
                    FirebaseErrorReporter()
                )
            )
        }
    }

}