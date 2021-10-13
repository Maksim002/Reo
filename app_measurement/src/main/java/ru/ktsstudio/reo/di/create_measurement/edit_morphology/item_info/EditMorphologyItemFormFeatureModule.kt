package ru.ktsstudio.reo.di.create_measurement.edit_morphology.item_info

import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.form_feature.FormActor
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.form_feature.field.FormFieldState
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.form.MeasurementFormStateMapper
import ru.ktsstudio.reo.domain.measurement.form.MeasurementFormValidator

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
@Module
object EditMorphologyItemFormFeatureModule {

    @Provides
    internal fun provideFormFeature(): Feature<
        FormFeature.Wish<MeasurementForm>,
        FormFeature.State,
        Nothing
        > {
        return FormFeature(
            initialState = FormFeature.State(
                isEmpty = true,
                formState = mapOf(
                    ContainerDataType.WasteVolumeDaily to FormFieldState.Valid(
                        isDisplayable = false,
                    ),
                    ContainerDataType.WasteWeightDaily to FormFieldState.Valid(
                        isDisplayable = false,
                    ),
                ),
                fieldInFocus = null,
                withDelayedState = true
            ),
            actor = FormActor(
                validator = MeasurementFormValidator(),
                formStateMapper = MeasurementFormStateMapper()
            )
        )
    }
}