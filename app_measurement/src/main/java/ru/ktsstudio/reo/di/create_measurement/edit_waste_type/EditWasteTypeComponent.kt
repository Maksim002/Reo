package ru.ktsstudio.reo.di.create_measurement.edit_waste_type

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.ContainerId
import ru.ktsstudio.common.di.qualifiers.WasteTypeId
import ru.ktsstudio.reo.ui.measurement.edit_waste_type.EditWasteTypeFragment

@Subcomponent(
    modules = [EditWasteTypeModule::class, EditWasteTypeFeatureModule::class, EditWasteTypeFormFeatureModule::class]
)
interface EditWasteTypeComponent {
    fun inject(fragment: EditWasteTypeFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @ContainerId containerId: Long,
            @BindsInstance @WasteTypeId wasteTypeId: String?,
        ): EditWasteTypeComponent
    }
}
