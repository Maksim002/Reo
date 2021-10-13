package ru.ktsstudio.feature_auth.domain.form.field

import ru.ktsstudio.form_feature.field.FieldError
import ru.ktsstudio.feature_auth.R

/**
 * Created by Igor Park on 25/09/2020.
 */
internal sealed class LoginFieldError: FieldError {
    sealed class Email : LoginFieldError() {

        object Empty : Email() {
            override val messageRes: Int = R.string.form_field_error_empty
        }

        object MaxLengthExceeded : Email() {
            override val messageRes: Int = R.string.form_field_error_email_max_length
        }

        object IncorrectFormat : Email() {
            override val messageRes: Int = R.string.form_field_error_email_format
        }
    }

    sealed class Password : LoginFieldError() {
        object Empty : Password() {
            override val messageRes: Int = R.string.form_field_error_empty
        }

        object MaxLengthError : Password() {
            override val messageRes: Int = R.string.form_field_error_password_max_length
        }

        object MinLengthError : Password() {
            override val messageRes: Int = R.string.form_field_error_password_min_length
        }

        object UpperCaseError : Password() {
            override val messageRes: Int = R.string.form_field_error_upper_case
        }

        object LowerCaseError : Password() {
            override val messageRes: Int = R.string.form_field_error_lower_case
        }

        object NumberError : Password() {
            override val messageRes: Int = R.string.form_field_error_number
        }

        object SpecialSymbolError : Password() {
            override val messageRes: Int = R.string.form_field_error_special_symbol
        }
    }
}