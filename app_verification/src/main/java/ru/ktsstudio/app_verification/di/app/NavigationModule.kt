package ru.ktsstudio.app_verification.di.app

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.app_verification.navigation.Navigator
import ru.ktsstudio.app_verification.navigation.ObjectNavigator
import ru.ktsstudio.common.di.AppScope
import ru.ktsstudio.common.navigation.api.AuthFailedNavigator
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.settings.navigation.SettingsNavigator

/**
 * Created by Igor Park on 30/09/2020.
 */
@Module
internal abstract class NavigationModule {

    @Binds
    abstract fun bindsAuthNavigator(navigator: Navigator): AuthNavigator

    @Binds
    abstract fun bindsRecycleMapNavigator(navigator: Navigator): RecycleMapNavigator

    @Binds
    abstract fun bindsObjectNavigator(navigator: Navigator): ObjectNavigator

    @Binds
    abstract fun bindsSyncNavigator(navigator: Navigator): SyncNavigator

    @Binds
    abstract fun bindsSettingsNavigator(navigator: Navigator): SettingsNavigator

    @Binds
    abstract fun bindsAuthFailedNavigator(navigator: Navigator): AuthFailedNavigator

    companion object {
        @Provides
        @JvmStatic
        @AppScope
        fun provideNavigator(): Navigator = Navigator()
    }
}
