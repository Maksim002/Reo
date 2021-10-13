package ru.ktsstudio.reo.app

import ru.ktsstudio.common.di.CoreApiComponent
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common.di.glide.GlideComponent
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement.CoreDataComponentFactory
import ru.ktsstudio.core_network.NetworkComponentFactory
import ru.ktsstudio.feature_auth.di.core.CoreAuthComponent
import ru.ktsstudio.feature_mno_list.di.MnoNavigationApi
import ru.ktsstudio.feature_sync_api.di.CoreSyncDependency
import ru.ktsstudio.reo.BuildConfig
import ru.ktsstudio.reo.di.app.DaggerMeasurementAppComponent
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.di.filter.FilterComponent
import ru.ktsstudio.reo.di.measurement.MeasurementNavigationApi
import ru.ktsstudio.settings.di.SettingsNavigationApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
class ComponentInitializer(
    private val app: MeasurementApp
) {
    fun initialize() {
        val coreApi = CoreApiComponent.create(
            context = app,
            appCodeProvider = app,
            appVersion = BuildConfig.VERSION_NAME
        )
        val networkApi = NetworkComponentFactory.create(
            coreApi = coreApi,
            apiUrl = BuildConfig.API_URL,
            authApiUrl = BuildConfig.AUTH_API_URL,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET
        )

        val coreDataApi = CoreDataComponentFactory.create(
            coreApi = coreApi,
            coreNetworkApi = networkApi,
            authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
        )

        ComponentRegistry.register { coreDataApi }
        ComponentRegistry.register { coreApi }
        ComponentRegistry.register { networkApi }

        val filterComponent = FilterComponent.create()

        val authCoreApi = CoreAuthComponent.create(
            coreDataApi.fileManager(),
            coreDataApi.databaseCleaner(),
            filterComponent.logoutCleaner()
        )
        ComponentRegistry.register<CoreAuthApi> { authCoreApi }

        val glideComponent = GlideComponent.create(
            networkApi.mediaOkHttpClient()
        )
        ComponentRegistry.register { glideComponent }

        val appComponent = DaggerMeasurementAppComponent.factory().create(
            coreApi = coreApi,
            coreNetworkApi = networkApi,
            coreAuthApi = authCoreApi,
            coreDataApi = coreDataApi,
            filterApi = filterComponent
        )
        ComponentRegistry.register<CoreSyncDependency> { appComponent }
        ComponentRegistry.register<ModularNavigationApi> { appComponent }
        ComponentRegistry.register<MeasurementNavigationApi> { appComponent }
        ComponentRegistry.register<MnoNavigationApi> { appComponent }
        ComponentRegistry.register<SettingsNavigationApi> { appComponent }
        ComponentRegistry.register<MeasurementAppComponent> { appComponent }
    }
}
