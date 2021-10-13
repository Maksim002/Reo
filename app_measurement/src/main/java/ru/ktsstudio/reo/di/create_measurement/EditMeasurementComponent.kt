package ru.ktsstudio.reo.di.create_measurement

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.reo.di.create_measurement.add_container.AddContainerComponent
import ru.ktsstudio.reo.di.create_measurement.create.CreateMeasurementComponent
import ru.ktsstudio.reo.di.create_measurement.edit_mixed_container.EditMixedContainerComponent
import ru.ktsstudio.reo.di.create_measurement.edit_morphology.item_info.EditMorphologyItemComponent
import ru.ktsstudio.reo.di.create_measurement.edit_morphology.section.EditMorphologyComponent
import ru.ktsstudio.reo.di.create_measurement.edit_separate_container.EditSeparateContainerComponent
import ru.ktsstudio.reo.di.create_measurement.edit_waste_type.EditWasteTypeComponent
import ru.ktsstudio.reo.di.measurement.MeasurementCommonModule
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder

/**
 * Created by Igor Park on 18/11/2020.
 */
@Subcomponent(
    modules = [DraftHolderModule::class, MeasurementCommonModule::class]
)
@FeatureScope
interface EditMeasurementComponent {
    fun draftHolder(): MeasurementDraftHolder

    fun createMeasurementComponentFactory(): CreateMeasurementComponent.Factory
    fun addContainerComponent(): AddContainerComponent
    fun editMixedContainerComponentFactory(): EditMixedContainerComponent.Factory
    fun editSeparateContainerComponentFactory(): EditSeparateContainerComponent.Factory
    fun editWasteTypeComponentFactory(): EditWasteTypeComponent.Factory
    fun editMorphologyComponentFactory(): EditMorphologyComponent.Factory
    fun editMorphologyItemComponentFactory(): EditMorphologyItemComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @Id mnoId: String): EditMeasurementComponent
    }
}
