package ru.ktsstudio.feature_auth.domain.auth

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AuthReducer : Reducer<AuthFeature.State, AuthFeature.Effect> {
    override fun invoke(state: AuthFeature.State, effect: AuthFeature.Effect): AuthFeature.State {
        return when (effect) {
            AuthFeature.Effect.Loading -> state.copy(
                isLoading = true,
                isIncorrectData = false
            )
            is AuthFeature.Effect.AuthError -> {
                val isIncorrectData = effect.throwable is IncorrectAuthDataException
                state.copy(
                    isLoading = false,
                    isIncorrectData = isIncorrectData
                )
            }
            is AuthFeature.Effect.NewInput -> {
                state.copy(
                    form = effect.form,
                    isIncorrectData = false
                )
            }

            is AuthFeature.Effect.AuthSuccess -> state
        }
    }
}