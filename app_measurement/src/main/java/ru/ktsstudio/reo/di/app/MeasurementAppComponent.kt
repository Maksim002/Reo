package ru.ktsstudio.reo.di.app

import dagger.Component
import ru.ktsstudio.common.di.AppScope
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.feature_mno_list.di.MnoNavigationApi
import ru.ktsstudio.feature_sync_api.di.CoreSyncDependency
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.di.create_measurement.start.StartMeasurementComponent
import ru.ktsstudio.reo.di.filter.FilterApi
import ru.ktsstudio.reo.di.map.MapFeatureDependencyComponent
import ru.ktsstudio.reo.di.measurement.MeasurementNavigationApi
import ru.ktsstudio.reo.di.measurement.list.MeasurementListComponent
import ru.ktsstudio.reo.di.mno_filter.MnoFilterComponent
import ru.ktsstudio.reo.di.mno_list.MnoListDependencyComponent
import ru.ktsstudio.reo.di.sync.SyncModule
import ru.ktsstudio.reo.ui.main.MainActivity
import ru.ktsstudio.settings.di.SettingsNavigationApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
@Component(
    dependencies = [
        CoreApi::class,
        CoreNetworkApi::class,
        CoreAuthApi::class,
        CoreMeasurementDataApi::class,
        FilterApi::class
    ],
    modules = [
        NavigationModule::class,
        SyncModule::class
    ]
)
@AppScope
internal interface MeasurementAppComponent : CoreApi,
    CoreNetworkApi,
    CoreAuthApi,
    CoreMeasurementDataApi,
    CoreSyncDependency,
    MnoNavigationApi,
    SettingsNavigationApi,
    MeasurementNavigationApi,
    ModularNavigationApi {

    fun inject(activity: MainActivity)

    fun mapDependencyComponent(): MapFeatureDependencyComponent
    fun mnoListDependencyComponent(): MnoListDependencyComponent
    fun mnoFilterComponent(): MnoFilterComponent
    fun measurementListComponent(): MeasurementListComponent
    fun startMeasurementComponent(): StartMeasurementComponent

    fun editMeasurementComponentFactory(): EditMeasurementComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            coreApi: CoreApi,
            coreNetworkApi: CoreNetworkApi,
            coreAuthApi: CoreAuthApi,
            coreDataApi: CoreMeasurementDataApi,
            filterApi: FilterApi
        ): MeasurementAppComponent
    }
}
