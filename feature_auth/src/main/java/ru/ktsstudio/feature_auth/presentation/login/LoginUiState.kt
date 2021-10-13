package ru.ktsstudio.feature_auth.presentation.login

import ru.ktsstudio.form_feature.FormState
import ru.ktsstudio.form_feature.field.FormFieldState
import ru.ktsstudio.utilities.extensions.orFalse

internal data class LoginUiState(
    val formState: FormState = emptyMap(),
    val isLoading: Boolean = false,
    val isIncorrectData: Boolean = false
)