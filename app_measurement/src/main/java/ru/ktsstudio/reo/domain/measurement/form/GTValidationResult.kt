package ru.ktsstudio.reo.domain.measurement.form

import ru.ktsstudio.form_feature.form_validation.EmptyCheck
import ru.ktsstudio.form_feature.form_validation.ValidationResult

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
class GTValidationResult<T>(
    override val isGreatThen: Boolean,
    override val isNotEmpty: Boolean,
) : ValidationResult, EmptyCheck, GreatThenCheck {
    override val isValid: Boolean
        get() = isGreatThen && isNotEmpty
}
