package ru.ktsstudio.feature_auth.presentation.login

import ru.ktsstudio.feature_auth.domain.auth.AuthFeature
import ru.ktsstudio.feature_auth.domain.form.LoginForm
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.form_feature.Qualifier

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.09.2020.
 */
internal sealed class LoginUiEvent {

    data class NewInput(
        val form: LoginForm
    ) : LoginUiEvent()

    data class SwitchField(
        val fieldType: Qualifier,
        val hasFocus: Boolean
    ) : LoginUiEvent()

    object SubmitLogin : LoginUiEvent()

    companion object {
        fun LoginUiEvent.toAuthFeatureWish(): AuthFeature.Wish? {
            return when (this) {
                is NewInput -> AuthFeature.Wish.NewInput(form)
                is SubmitLogin -> AuthFeature.Wish.Submit
                is SwitchField -> null
            }
        }

        fun LoginUiEvent.toFormFeatureWish(): FormFeature.Wish<LoginForm>? {
            return when (this) {
                is SwitchField -> FormFeature.Wish.SwitchField(
                    field = fieldType,
                    hasFocus = hasFocus
                )
                is NewInput -> FormFeature.Wish.Input(form = form)
                is SubmitLogin -> null
            }
        }
    }
}