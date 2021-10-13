package ru.ktsstudio.feature_auth.di.flow.modules

import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.feature_auth.domain.auth.AuthActor
import ru.ktsstudio.feature_auth.domain.auth.AuthNewsPublisher
import ru.ktsstudio.feature_auth.domain.auth.AuthFeature
import ru.ktsstudio.feature_auth.domain.auth.AuthReducer
import ru.ktsstudio.feature_auth.domain.form.LoginForm
import ru.ktsstudio.feature_auth.presentation.login.LoginViewModel

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Module
internal interface LoginModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindViewModel(impl: LoginViewModel): ViewModel

    companion object {

        @Provides
        fun provideAuthFeature(
            initialState: AuthFeature.State,
            schedulers: SchedulerProvider,
            authRepository: AuthRepository
        ): Feature<
            AuthFeature.Wish,
            AuthFeature.State,
            AuthFeature.News
            > {
            return AuthFeature(
                initialState = initialState,
                actor = AuthActor(
                    authRepository,
                    schedulers
                ),
                reducer = AuthReducer(),
                newsPublisher = AuthNewsPublisher()
            )
        }

        @Provides
        fun provideInitialState(): AuthFeature.State {
            return AuthFeature.State(
                form = LoginForm.EMPTY,
                isLoading = false,
                isIncorrectData = false
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}