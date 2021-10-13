package ru.ktsstudio.reo.di.map.mno_info

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.reo.ui.map.MnoInfoDialogFragment

@Component(
    dependencies = [CoreApi::class, CoreMeasurementDataApi::class],
    modules = [MnoInfoModule::class]
)
interface MnoInfoComponent {
    fun inject(fragment: MnoInfoDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectIds: List<String>,
            coreApi: CoreApi,
            coreDataApi: CoreMeasurementDataApi
        ): MnoInfoComponent
    }

    companion object {
        fun create(objectIds: List<String>): MnoInfoComponent {
            return DaggerMnoInfoComponent.factory().create(
                objectIds = objectIds,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
