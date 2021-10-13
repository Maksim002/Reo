package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.common.utils.form.setError
import ru.ktsstudio.form_feature.FormState
import ru.ktsstudio.form_feature.field.FieldError
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.11.2020.
 */
class MeasurementFormStateMapper : (MeasurementValidationResult, FormState) -> FormState {
    override fun invoke(result: MeasurementValidationResult, currentState: FormState): FormState {
        return currentState.mapValues { (field, fieldState) ->
            field as ContainerDataType
            val fieldError = getFieldError(field, result)
            fieldState.setError(fieldError)
        }
    }

    private fun getFieldError(
        field: ContainerDataType,
        validationResult: MeasurementValidationResult
    ): FieldError? {
        return when (field) {
            ContainerDataType.WasteVolume -> validationResult.volume
                .mapError(MeasurementFormError.WasteVolume.MinValue)
            ContainerDataType.WasteVolumeDaily -> validationResult.dailyVolume
                .mapError(MeasurementFormError.WasteVolumeDaily.MinValue)
            ContainerDataType.WasteWeight -> validationResult.weight
                .mapError(MeasurementFormError.WasteWeight.MinValue)
            ContainerDataType.WasteWeightDaily -> validationResult.dailyWeight
                .mapError(MeasurementFormError.WasteWeightDaily.MinValue)
            else -> null
        }
    }

    private fun GTEValidationResult<Float>.mapError(
        onMinLength: FieldError,
    ): FieldError? {
        return when {
            isGreatOrEqual.not() -> onMinLength
            else -> null
        }
    }

    private fun GTValidationResult<Float>.mapError(
        onMinLength: FieldError,
    ): FieldError? {
        return when {
            isGreatThen.not() -> onMinLength
            else -> null
        }
    }
}
