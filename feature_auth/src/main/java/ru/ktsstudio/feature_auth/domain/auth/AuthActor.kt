package ru.ktsstudio.feature_auth.domain.auth

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.feature_auth.domain.form.LoginForm

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.09.2020.
 */
internal class AuthActor(
    private val authRepository: AuthRepository,
    private val schedulers: SchedulerProvider
) : Actor<AuthFeature.State, AuthFeature.Wish, AuthFeature.Effect> {
    override fun invoke(
        state: AuthFeature.State,
        action: AuthFeature.Wish
    ): Observable<out AuthFeature.Effect> {
        return when (action) {
            is AuthFeature.Wish.NewInput -> Observable.just(
                AuthFeature.Effect.NewInput(action.form)
            )
            is AuthFeature.Wish.Submit -> {
                login(form = state.form)
            }
        }
    }

    private fun login(form: LoginForm): Observable<AuthFeature.Effect> {
        return authRepository.login(email = form.email, password = form.password)
            .observeOn(schedulers.ui)
            .toSingleDefault<AuthFeature.Effect>(AuthFeature.Effect.AuthSuccess)
            .toObservable()
            .startWithItem(AuthFeature.Effect.Loading)
            .onErrorReturn {
                AuthFeature.Effect.AuthError(it)
            }
            .toRx2Observable()
    }
}