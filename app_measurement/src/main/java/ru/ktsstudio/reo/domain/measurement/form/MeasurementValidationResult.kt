package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.form_feature.form_validation.ValidationResult

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
class MeasurementValidationResult(
    val weight: GTEValidationResult<Float>,
    val volume: GTEValidationResult<Float>,
    val dailyWeight: GTValidationResult<Float>,
    val dailyVolume: GTValidationResult<Float>,
) : ValidationResult {
    override val isValid: Boolean
        get() = weight.isValid &&
            volume.isValid &&
            dailyWeight.isValid &&
            dailyVolume.isValid
}
