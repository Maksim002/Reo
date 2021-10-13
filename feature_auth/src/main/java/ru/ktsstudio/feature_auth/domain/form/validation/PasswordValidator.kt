package ru.ktsstudio.feature_auth.domain.form.validation

import ru.ktsstudio.form_feature.form_validation.ValidationResult
import ru.ktsstudio.form_feature.form_validation.Validator

internal class PasswordValidator : Validator<String, ValidationResult>() {
    override fun validate(input: String): PasswordValidationResult {
        val hasMinLength = input.length >= PASSWORD_MIN_LENGTH
        val hasMaxLength = input.length < PASSWORD_MAX_LENGTH
        val hasUpperCase = input.matches(UPPER_CASE_CHECK)
        val hasLowerCase = input.matches(LOWER_CASE_CHECK)
        val hasNumber = input.matches(NUMBER_CHECK)
        val hasSpecialSymbol = input.matches(SYMBOL_CHECK)

        return PasswordValidationResult(
            isNotEmpty = input.isNotEmpty(),
            isLengthAcceptable = hasMaxLength,
            hasMinLength = hasMinLength,
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase,
            hasNumber = hasNumber,
            hasSpecialSymbol = hasSpecialSymbol
        )
    }

    companion object {
        private val UPPER_CASE_CHECK = ".*\\p{Upper}+.*".toRegex()
        private val LOWER_CASE_CHECK = ".*\\p{Lower}+.*".toRegex()
        private val NUMBER_CHECK = ".*\\p{Digit}+.*".toRegex()
        private val SYMBOL_CHECK = ".*[!#\\\\$%&*@]+.*".toRegex()
        private const val PASSWORD_MAX_LENGTH = 320
        private const val PASSWORD_MIN_LENGTH = 8
    }
}