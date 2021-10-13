package ru.ktsstudio.feature_auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.mvi.combineLatest
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.feature_auth.domain.auth.AuthFeature
import ru.ktsstudio.feature_auth.domain.form.LoginForm
import ru.ktsstudio.feature_auth.presentation.auth_state.AuthStatusHandler
import ru.ktsstudio.feature_auth.presentation.login.LoginUiEvent.Companion.toAuthFeatureWish
import ru.ktsstudio.feature_auth.presentation.login.LoginUiEvent.Companion.toFormFeatureWish
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.utilities.extensions.orTrue
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class LoginViewModel @Inject constructor(
    private val authFeature: Feature<AuthFeature.Wish, AuthFeature.State, AuthFeature.News>,
    private val formFeature: Feature<FormFeature.Wish<LoginForm>, FormFeature.State, Nothing>,
    private val binder: Binder,
    private val authStatusHandler: AuthStatusHandler
) : RxViewModel(), Consumer<LoginUiState> {

    private val uiEventsSubject = Rx2PublishSubject.create<LoginUiEvent>()

    private val stateMutableLiveData = MutableLiveData<LoginUiState>()
    private val navigateMutableLiveData = MutableLiveData<Unit>()
    private val errorMessageMutableLiveData = SingleLiveEvent<String>()
    private val formStateIsInitialized: Boolean
        get() = stateMutableLiveData.value
            ?.formState
            ?.isNotEmpty()
            .orTrue()

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: LoginUiState) {
        stateMutableLiveData.postValue(state)
    }

    private fun setupBindings() = with(binder) {
        bind(uiEventsSubject to formFeature using { it.toFormFeatureWish() })
        bind(uiEventsSubject to authFeature using { it.toAuthFeatureWish() })
        bind(
            combineLatest(
                formFeature,
                authFeature
            ) to this@LoginViewModel using LoginStateTransformer()
        )
        bind(authFeature.news to ::reportAuthNews.toConsumer())
    }

    val loginState: LiveData<LoginUiState>
        get() = stateMutableLiveData
    val navigate: LiveData<Unit>
        get() = navigateMutableLiveData
    val errorMessage: LiveData<String>
        get() = errorMessageMutableLiveData

    fun login() {
        uiEventsSubject.onNext(LoginUiEvent.SubmitLogin)
    }

    fun switchField(event: LoginUiEvent.SwitchField) {
        uiEventsSubject.onNext(event)
    }

    fun listenLoginForm(
        loginObservable: Observable<CharSequence>,
        passwordObservable: Observable<CharSequence>
    ) {
        Observable.combineLatest(
            loginObservable,
            passwordObservable,
            BiFunction { login: CharSequence, password: CharSequence ->
                LoginForm(login.toString(), password.toString())
            }
        )
            .skipFirstIf { formStateIsInitialized }
            .distinctUntilChanged()
            .map(LoginUiEvent::NewInput)
            .subscribe(uiEventsSubject::onNext, Timber::e)
            .storeForeground()
    }

    private fun reportAuthNews(news: AuthFeature.News) {
        when (news) {
            is AuthFeature.News.Status -> {
                authStatusHandler.handleAuthStatus(
                    authStatus = news.status,
                    doOnComplete = {
                        navigateMutableLiveData.value = Unit
                    },
                    doOnFail = { errorMessage ->
                        errorMessageMutableLiveData.value = errorMessage
                    }
                )
            }
        }
    }

    private fun <T> Observable<T>.skipFirstIf(predicate: (T) -> Boolean): Observable<T> {
        return publish {
            Observable.merge(
                it.take(1)
                    .filter(predicate),
                it.skip(1)
            )
        }
    }
}