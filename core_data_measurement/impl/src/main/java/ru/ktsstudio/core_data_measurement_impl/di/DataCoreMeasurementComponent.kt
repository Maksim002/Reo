package ru.ktsstudio.core_data_measurement_impl.di

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Authority
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.core_network_api.CoreNetworkApi

@Component(
    dependencies = [DataCoreMeasurementDependencies::class],
    modules = [CoreMeasurementDataModule::class]
)
@FeatureScope
interface DataCoreMeasurementComponent : CoreMeasurementDataApi {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DataCoreMeasurementDependencies): CoreMeasurementDataApi
    }

    @Component(dependencies = [CoreApi::class, CoreNetworkApi::class])
    interface DataCoreDependenciesComponent : DataCoreMeasurementDependencies {

        @Component.Factory
        interface Factory {
            fun create(
                coreApi: CoreApi,
                coreNetworkApi: CoreNetworkApi,
                @BindsInstance @Authority fileProviderAuthority: String
            ): DataCoreDependenciesComponent
        }
    }

    companion object {
        fun create(
            coreApi: CoreApi,
            coreNetworkApi: CoreNetworkApi,
            fileProviderAuthority: String
        ): CoreMeasurementDataApi = DaggerDataCoreMeasurementComponent.factory().create(
            DaggerDataCoreMeasurementComponent_DataCoreDependenciesComponent.factory()
                .create(coreApi, coreNetworkApi, fileProviderAuthority)
        )
    }
}