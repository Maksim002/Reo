package ru.ktsstudio.feature_auth.domain.form.validation

import ru.ktsstudio.form_feature.form_validation.FormattedTextValidationResult
import ru.ktsstudio.form_feature.form_validation.ValidationResult

/**
 * Created by Igor Park on 25/09/2020.
 */
internal data class LoginFormValidationResult(
    val email: FormattedTextValidationResult,
    val password: PasswordValidationResult
) : ValidationResult {
    override val isValid: Boolean = email.isValid && password.isValid
}