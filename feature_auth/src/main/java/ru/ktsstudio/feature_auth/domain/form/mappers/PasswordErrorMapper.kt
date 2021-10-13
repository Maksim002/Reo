package ru.ktsstudio.feature_auth.domain.form.mappers

import ru.ktsstudio.feature_auth.domain.form.validation.PasswordValidationResult
import ru.ktsstudio.form_feature.field.FieldError

/**
 * Created by Igor Park on 25/09/2020.
 */
internal object PasswordErrorMapper {
    fun map(
        validationResult: PasswordValidationResult,
        onEmpty: () -> FieldError,
        onLengthNotAcceptable: () -> FieldError,
        onMinLengthError: () -> FieldError,
        onUpperCaseError: () -> FieldError,
        onLowerCaseError: () -> FieldError,
        onNumberError: () -> FieldError,
        onSpecialSymbolError: () -> FieldError
    ): FieldError? {
        val emptyError = validationResult.isNotEmpty.not()
        val maxLengthError = validationResult.isLengthAcceptable.not()
        val minLengthError = validationResult.hasMinLength.not()
        val upperCaseError = validationResult.hasUpperCase.not()
        val lowerCaseError = validationResult.hasLowerCase.not()
        val numberError = validationResult.hasNumber.not()
        val specialSymbolError = validationResult.hasSpecialSymbol.not()

        return when {
            emptyError -> onEmpty()
            minLengthError -> onMinLengthError()
            maxLengthError -> onLengthNotAcceptable()
            upperCaseError -> onUpperCaseError()
            lowerCaseError -> onLowerCaseError()
            numberError -> onNumberError()
            specialSymbolError -> onSpecialSymbolError()
            validationResult.isValid -> null
            else -> error("Unreachable condition")
        }
    }
}