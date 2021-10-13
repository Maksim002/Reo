package ru.ktsstudio.feature_auth.presentation.login.form

import android.widget.TextView
import androidx.core.view.isInvisible
import ru.ktsstudio.feature_auth.utils.OutlinedEditText
import ru.ktsstudio.form_feature.field_view.FieldView

/**
 * Created by Igor Park on 25/09/2020.
 */
internal data class FieldViewImpl(
    private val input: OutlinedEditText,
    private val errorMessage: TextView
) : FieldView {

    override fun displayErrorMessage(show: Boolean, message: Int?) {
        input.errorState = show
        errorMessage.isInvisible = show.not()
        message?.let(errorMessage::setText)
    }
}