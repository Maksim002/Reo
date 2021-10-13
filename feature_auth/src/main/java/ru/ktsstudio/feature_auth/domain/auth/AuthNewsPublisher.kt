package ru.ktsstudio.feature_auth.domain.auth

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.09.2020.
 */
internal class AuthNewsPublisher :
    NewsPublisher<AuthFeature.Wish, AuthFeature.Effect, AuthFeature.State, AuthFeature.News> {
    override fun invoke(
        action: AuthFeature.Wish,
        effect: AuthFeature.Effect,
        state: AuthFeature.State
    ): AuthFeature.News? {
        return when(effect) {
            AuthFeature.Effect.AuthSuccess -> AuthFeature.News.Status(AuthStatus.Complete)
            is AuthFeature.Effect.AuthError -> {
                val isIncorrectAuth = effect.throwable is IncorrectAuthDataException
                AuthFeature.News.Status(AuthStatus.Failed(effect.throwable)).takeIf { isIncorrectAuth.not() }
            }
            else -> null
        }
    }
}