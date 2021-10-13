package ru.ktsstudio.feature_auth.domain.form.validation

import ru.ktsstudio.form_feature.form_validation.EmptyCheck
import ru.ktsstudio.form_feature.form_validation.MaxLengthCheck
import ru.ktsstudio.form_feature.form_validation.ValidationResult

/**
 * Created by Igor Park on 25/09/2020.
 */
internal class PasswordValidationResult(
    override val isNotEmpty: Boolean,
    override val isLengthAcceptable: Boolean,
    val hasMinLength: Boolean,
    val hasUpperCase: Boolean,
    val hasLowerCase: Boolean,
    val hasNumber: Boolean,
    val hasSpecialSymbol: Boolean
) : ValidationResult, EmptyCheck, MaxLengthCheck {
    override val isValid: Boolean = isNotEmpty &&
        hasMinLength &&
        isLengthAcceptable &&
        hasUpperCase &&
        hasLowerCase &&
        hasNumber &&
        hasSpecialSymbol
}