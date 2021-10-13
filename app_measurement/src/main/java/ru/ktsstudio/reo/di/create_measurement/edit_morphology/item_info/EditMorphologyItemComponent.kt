package ru.ktsstudio.reo.di.create_measurement.edit_morphology.item_info

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.reo.ui.measurement.morphology.item_info.EditMorphologyItemFragment
import javax.inject.Named

@Subcomponent(
    modules = [
        EditMorphologyItemModule::class,
        EditMorphologyItemFeatureModule::class,
        EditMorphologyItemFormFeatureModule::class
    ]
)
interface EditMorphologyItemComponent {
    fun inject(fragment: EditMorphologyItemFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @MeasurementId measurementId: Long,
            @BindsInstance @Named("morphologyId") morphologyId: Long?
        ): EditMorphologyItemComponent
    }
}
