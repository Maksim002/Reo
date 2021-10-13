package ru.ktsstudio.reo.di.create_measurement.edit_separate_container

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.ContainerId
import ru.ktsstudio.common.di.qualifiers.ContianerTypeId
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.common.di.qualifiers.MnoContainerId
import ru.ktsstudio.reo.ui.measurement.edit_separate_container.EditSeparateContainerFragment

@Subcomponent(
    modules = [EditSeparateContainerModule::class, EditSeparateContainerFeatureModule::class]
)
interface EditSeparateContainerComponent {
    fun inject(fragment: EditSeparateContainerFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @MeasurementId measurementId: Long,
            @BindsInstance @ContainerId containerId: Long?,
            @BindsInstance @MnoContainerId mnoContainerId: String?,
            @BindsInstance @ContianerTypeId containerTypeId: String?,
        ): EditSeparateContainerComponent
    }
}
