package ru.ktsstudio.feature_auth.presentation.login

import ru.ktsstudio.feature_auth.domain.auth.AuthFeature
import ru.ktsstudio.form_feature.FormFeature

internal class LoginStateTransformer : (Pair<FormFeature.State, AuthFeature.State>) -> LoginUiState {

    override fun invoke(formToAuthState: Pair<FormFeature.State, AuthFeature.State>): LoginUiState {
        val formState = formToAuthState.first.formState
        val authState = formToAuthState.second
        return LoginUiState(
            formState = formState,
            isIncorrectData = authState.isIncorrectData,
            isLoading = authState.isLoading
        )
    }
}
