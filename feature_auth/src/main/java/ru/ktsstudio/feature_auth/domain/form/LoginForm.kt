package ru.ktsstudio.feature_auth.domain.form

import ru.ktsstudio.form_feature.form.CommonForm

/**
 * Created by Igor Park on 25/09/2020.
 */
internal data class LoginForm(
    val email: String,
    val password: String
) : CommonForm {
    override fun isEmpty(): Boolean {
        return this == EMPTY
    }

    companion object {
        val EMPTY = LoginForm("", "")
    }
}