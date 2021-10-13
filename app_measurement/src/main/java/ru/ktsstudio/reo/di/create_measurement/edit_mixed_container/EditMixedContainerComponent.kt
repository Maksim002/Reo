package ru.ktsstudio.reo.di.create_measurement.edit_mixed_container

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.ContainerId
import ru.ktsstudio.common.di.qualifiers.MnoContainerId
import ru.ktsstudio.common.di.qualifiers.ContianerTypeId
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.reo.ui.measurement.edit_mixed_container.EditMixedContainerFragment

@Subcomponent(
    modules = [
        EditMixedContainerModule::class,
        EditMixedContainerFeatureModule::class,
        EditMixedContainerFormFeatureModule::class
    ]
)
interface EditMixedContainerComponent {
    fun inject(fragment: EditMixedContainerFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @MeasurementId measurementId: Long,
            @BindsInstance @ContainerId containerId: Long?,
            @BindsInstance @MnoContainerId mnoContainerId: String?,
            @BindsInstance @ContianerTypeId containerTypeId: String?,
        ): EditMixedContainerComponent
    }
}
