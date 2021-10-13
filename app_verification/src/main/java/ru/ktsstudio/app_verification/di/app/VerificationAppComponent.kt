package ru.ktsstudio.app_verification.di.app

import dagger.Component
import ru.ktsstudio.app_verification.di.filter.FilterApi
import ru.ktsstudio.app_verification.di.map.MapFeatureDependencyComponent
import ru.ktsstudio.app_verification.di.object_filter.ObjectFilterComponent
import ru.ktsstudio.app_verification.di.object_list.ObjectNavigationApi
import ru.ktsstudio.app_verification.di.object_list.dependencies.ObjectListDependencyComponent
import ru.ktsstudio.app_verification.di.sync.SyncModule
import ru.ktsstudio.app_verification.ui.MainActivity
import ru.ktsstudio.common.di.AppScope
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.feature_sync_api.di.CoreSyncDependency
import ru.ktsstudio.settings.di.SettingsNavigationApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
@Component(
    dependencies = [
        CoreApi::class,
        CoreNetworkApi::class,
        CoreAuthApi::class,
        CoreVerificationDataApi::class,
        FilterApi::class
    ],
    modules = [
        NavigationModule::class,
        SyncModule::class
    ]
)
@AppScope
internal interface VerificationAppComponent : CoreApi,
    CoreVerificationDataApi,
    CoreNetworkApi,
    CoreSyncDependency,
    ObjectNavigationApi,
    ModularNavigationApi,
    SettingsNavigationApi {

    fun inject(activity: MainActivity)

    fun mapDependencyComponent(): MapFeatureDependencyComponent
    fun objectListDependencyComponent(): ObjectListDependencyComponent
    fun objectFilterComponent(): ObjectFilterComponent

    @Component.Factory
    interface Factory {
        fun create(
            coreApi: CoreApi,
            coreNetworkApi: CoreNetworkApi,
            coreAuthApi: CoreAuthApi,
            coreDataApi: CoreVerificationDataApi,
            filterApi: FilterApi
        ): VerificationAppComponent
    }
}
