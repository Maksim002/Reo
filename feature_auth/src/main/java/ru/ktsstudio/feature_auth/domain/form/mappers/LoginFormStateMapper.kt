package ru.ktsstudio.feature_auth.domain.form.mappers

import ru.ktsstudio.common.utils.form.setError
import ru.ktsstudio.feature_auth.domain.form.field.LoginFieldType
import ru.ktsstudio.feature_auth.domain.form.field.LoginFieldError
import ru.ktsstudio.feature_auth.domain.form.validation.LoginFormValidationResult
import ru.ktsstudio.form_feature.FormState
import ru.ktsstudio.form_feature.Qualifier
import ru.ktsstudio.form_feature.field.FieldError
import ru.ktsstudio.form_feature.field.FormFieldState
import ru.ktsstudio.form_feature.mappers.DefaultFormattedErrorMapper

internal class LoginFormStateMapper : (LoginFormValidationResult, FormState) -> FormState {
    override fun invoke(
        validationResult: LoginFormValidationResult,
        currentFormState: FormState
    ): Map<Qualifier, FormFieldState> {
        return currentFormState.mapValues { (field, fieldState) ->
            val fieldError = getFieldError(
                validationResult = validationResult,
                field = field as LoginFieldType
            )
            fieldState.setError(fieldError)
        }
    }

    private fun getFieldError(
        field: LoginFieldType,
        validationResult: LoginFormValidationResult
    ): FieldError? {
        return when (field) {
            is LoginFieldType.Email -> {
                val result = validationResult.email
                DefaultFormattedErrorMapper.map(
                    validationResult = result,
                    onEmpty = { LoginFieldError.Email.Empty },
                    onLengthNotAcceptable = { LoginFieldError.Email.MaxLengthExceeded },
                    onIncorrectFormat = { LoginFieldError.Email.IncorrectFormat },
                )
            }

            is LoginFieldType.Password -> {
                val result = validationResult.password
                PasswordErrorMapper.map(
                    validationResult = result,
                    onEmpty = { LoginFieldError.Password.Empty },
                    onMinLengthError = { LoginFieldError.Password.MinLengthError },
                    onLengthNotAcceptable = { LoginFieldError.Password.MaxLengthError },
                    onUpperCaseError = { LoginFieldError.Password.UpperCaseError },
                    onLowerCaseError = { LoginFieldError.Password.LowerCaseError },
                    onNumberError = { LoginFieldError.Password.NumberError },
                    onSpecialSymbolError = { LoginFieldError.Password.SpecialSymbolError }
                )
            }
        }
    }
}
