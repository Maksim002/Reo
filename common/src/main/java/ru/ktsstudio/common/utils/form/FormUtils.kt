package ru.ktsstudio.common.utils.form

import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.form_feature.FormState
import ru.ktsstudio.form_feature.field.FieldError
import ru.ktsstudio.form_feature.field.FormFieldState
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.11.2020.
 */

val FormState.isCorrect: Boolean
    get() = values
        .takeIf { it.isNotEmpty() }
        ?.all { fieldState ->
            fieldState is FormFieldState.Valid
        }
        .orFalse()

fun FormFieldState.setError(fieldError: FieldError?): FormFieldState {
    if (fieldError == null) return FormFieldState.Valid(
        isDisplayable = isDisplayable
    )

    return when (this) {
        is FormFieldState.Valid -> FormFieldState.ErrorState(
            isDisplayable = isDisplayable,
            fieldError = fieldError
        )
        is FormFieldState.ErrorState -> copy(fieldError = fieldError)
    }
}

fun <T: Any> FormFeature.State.getFieldError(field: T): Int? {
    return formState[field]?.let { it as? FormFieldState.ErrorState }?.fieldError?.messageRes
}