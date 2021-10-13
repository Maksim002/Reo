package ru.ktsstudio.feature_mno_list.di.details

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.feature_mno_list.di.MnoCommonModule
import ru.ktsstudio.feature_mno_list.di.MnoNavigationApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.feature_mno_list.ui.details.MnoDetailsFragment

/**
 * @author Maxim Ovchinnikov on 01.10.2020.
 */
@Component(
    dependencies = [CoreApi::class, MnoNavigationApi::class, CoreMeasurementDataApi::class],
    modules = [MnoCommonModule::class, MnoDetailsModule::class]
)
internal interface MnoDetailsComponent {
    fun inject(fragment: MnoDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id id: String,
            coreApi: CoreApi,
            mnoNavigationApi: MnoNavigationApi,
            coreDataApi: CoreMeasurementDataApi
        ): MnoDetailsComponent
    }

    companion object {
        fun create(id: String): MnoDetailsComponent {
            return DaggerMnoDetailsComponent.factory().create(
                id = id,
                coreApi = ComponentRegistry.get(),
                mnoNavigationApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}