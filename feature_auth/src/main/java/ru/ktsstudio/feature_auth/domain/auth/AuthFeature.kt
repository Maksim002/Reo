package ru.ktsstudio.feature_auth.domain.auth

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.feature_auth.domain.form.LoginForm

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AuthFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    AuthFeature.Wish,
    AuthFeature.Effect,
    AuthFeature.State,
    AuthFeature.News
    >(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    newsPublisher = newsPublisher,
    bootstrapper = null
) {

    data class State(
        val form: LoginForm,
        val isLoading: Boolean,
        val isIncorrectData: Boolean
    )

    sealed class Wish {
        data class NewInput(val form: LoginForm) : Wish()
        object Submit : Wish()
    }

    sealed class Effect {
        object AuthSuccess : Effect()
        data class AuthError(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect

        object Loading : Effect()
        data class NewInput(val form: LoginForm) : Effect()
    }

    sealed class News {
        data class Status(val status: AuthStatus) : News()
    }
}