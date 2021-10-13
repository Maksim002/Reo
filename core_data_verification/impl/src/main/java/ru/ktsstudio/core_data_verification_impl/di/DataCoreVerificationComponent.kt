package ru.ktsstudio.core_data_verification_impl.di

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Authority
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi
import ru.ktsstudio.core_network_api.CoreNetworkApi

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
@Component(
    dependencies = [DataCoreVerificationDependencies::class],
    modules = [CoreVerificationDataModule::class, MapperModule::class, DbModule::class]
)
@FeatureScope
interface DataCoreVerificationComponent : CoreVerificationDataApi {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DataCoreVerificationDependencies): CoreVerificationDataApi
    }

    @Component(dependencies = [CoreApi::class, CoreNetworkApi::class])
    interface DataCoreDependenciesComponent : DataCoreVerificationDependencies {

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
        ): CoreVerificationDataApi = DaggerDataCoreVerificationComponent.factory().create(
            DaggerDataCoreVerificationComponent_DataCoreDependenciesComponent.factory()
                .create(coreApi, coreNetworkApi, fileProviderAuthority)
        )
    }
}