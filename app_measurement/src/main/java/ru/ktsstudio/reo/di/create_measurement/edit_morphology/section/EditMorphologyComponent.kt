package ru.ktsstudio.reo.di.create_measurement.edit_morphology.section

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.reo.ui.measurement.morphology.section.EditMorphologyFragment

@Subcomponent(
    modules = [EditMorphologyModule::class, EditMorphologyFeatureModule::class]
)
interface EditMorphologyComponent {
    fun inject(fragment: EditMorphologyFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @MeasurementId measurementId: Long): EditMorphologyComponent
    }
}
