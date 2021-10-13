package ru.ktsstudio.common.utils.text_format.applier

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.common.utils.text_format.filters.TimeInputFilter
import ru.ktsstudio.utilities.extensions.string

/**
 * @author Maxim Ovchinnikov on 04.12.2020.
 */
class TimeFormatApplier : FormatApplier<TextFormat.Time> {

    override fun onFormatSet(editText: EditText, format: TextFormat.Time) = with(editText) {
        inputType = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME
        filters = arrayOf(
            TimeInputFilter()
        )
        addTextChangedListener(getTimeTextWatcher(this))
    }

    override fun onFormatUnset(editText: EditText) {
        editText.removeTextChangedListener(getTimeTextWatcher(editText))
    }

    private fun getTimeTextWatcher(editText: EditText): TextWatcher = with(editText) {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) return

                val text = string()
                if (text.length == 2) {
                    val updatedText = "$text:"
                    setValueWithoutEventTrigger(updatedText, this).also {
                        setSelection(updatedText.length)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }
}