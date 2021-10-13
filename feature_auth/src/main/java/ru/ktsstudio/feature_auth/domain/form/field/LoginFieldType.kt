package ru.ktsstudio.feature_auth.domain.form.field

/**
 * Created by Igor Park on 2019-09-26.
 */

internal sealed class LoginFieldType {
    object Email : LoginFieldType()
    object Password : LoginFieldType()
}
