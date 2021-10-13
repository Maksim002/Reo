package ru.ktsstudio.feature_auth.domain.form.validation

import ru.ktsstudio.feature_auth.domain.form.LoginForm
import ru.ktsstudio.form_feature.form_validation.EmailValidator
import ru.ktsstudio.form_feature.form_validation.Validator

/**
 * Created by Igor Park on 25/09/2020.
 */
internal class LoginFormValidator : Validator<LoginForm, LoginFormValidationResult>() {
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    override fun validate(input: LoginForm): LoginFormValidationResult = with(input) {
        LoginFormValidationResult(
            emailValidator.validate(email),
            passwordValidator.validate(password)
        )
    }
}