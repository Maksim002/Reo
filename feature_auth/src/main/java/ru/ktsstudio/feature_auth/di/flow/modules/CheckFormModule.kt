package ru.ktsstudio.feature_auth.di.flow.modules

import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.feature_auth.domain.form.LoginForm
import ru.ktsstudio.feature_auth.domain.form.field.LoginFieldError
import ru.ktsstudio.feature_auth.domain.form.field.LoginFieldType
import ru.ktsstudio.feature_auth.domain.form.mappers.LoginFormStateMapper
import ru.ktsstudio.feature_auth.domain.form.validation.LoginFormValidator
import ru.ktsstudio.form_feature.FormActor
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.form_feature.field.FormFieldState

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal object CheckFormModule {
    @Provides
    internal fun provideFormFeature(): Feature<
        FormFeature.Wish<LoginForm>,
        FormFeature.State,
        Nothing
        > {
        return FormFeature(
            initialState = FormFeature.State(
                isEmpty = true,
                formState = mapOf(
                    LoginFieldType.Email to FormFieldState.ErrorState(
                        fieldError = LoginFieldError.Email.Empty,
                        isDisplayable = false
                    ),
                    LoginFieldType.Password to FormFieldState.ErrorState(
                        fieldError = LoginFieldError.Password.Empty,
                        isDisplayable = false
                    )
                ),
                fieldInFocus = null,
                withDelayedState = true
            ),
            actor = FormActor(
                validator = LoginFormValidator(),
                formStateMapper = LoginFormStateMapper()
            )
        )
    }
}