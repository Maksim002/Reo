package ru.ktsstudio.reo.di.app

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.di.AppScope
import ru.ktsstudio.common.navigation.api.AuthFailedNavigator
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.feature_mno_list.navigation.MnoNavigator
import ru.ktsstudio.reo.navigation.MeasurementNavigator
import ru.ktsstudio.reo.navigation.Navigator
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
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
    abstract fun bindMnoListNavigator(impl: Navigator): MnoNavigator

    @Binds
    abstract fun bindMeasurementListNavigator(impl: Navigator): MeasurementNavigator

    @Binds
    abstract fun bindSyncNavigator(impl: Navigator): SyncNavigator

    @Binds
    abstract fun bindsEditMeasurementNavigator(navigator: Navigator): EditMeasurementNavigator

    @Binds
    abstract fun bindSettingsNavigator(impl: Navigator): SettingsNavigator

    @Binds
    abstract fun bindAuthFailedNavigator(impl: Navigator): AuthFailedNavigator

    companion object {
        @Provides
        @JvmStatic
        @AppScope
        fun provideNavigator(): Navigator = Navigator()
    }
}
