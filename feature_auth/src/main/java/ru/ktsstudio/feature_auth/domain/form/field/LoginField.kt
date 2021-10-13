package ru.ktsstudio.feature_auth.domain.form.field

import ru.ktsstudio.form_feature.field.DefaultFormField
import ru.ktsstudio.form_feature.field_view.FieldView

/**
 * Created by Igor Park on 2019-09-27.
 */

internal sealed class LoginField : DefaultFormField() {
    data class Email(
        override val fieldView: FieldView
    ) : LoginField() {
        override val qualifier: LoginFieldType =
            LoginFieldType.Email
    }

    data class Password(
        override val fieldView: FieldView
    ) : LoginField() {
        override val qualifier: LoginFieldType =
            LoginFieldType.Password
    }
}