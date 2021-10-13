package ru.ktsstudio.common.utils.text_format.applier

import android.text.InputType
import android.widget.EditText
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.common.utils.text_format.filters.MinMaxInputFilter

/**
 * @author Maxim Ovchinnikov on 04.12.2020.
 */
class NumberFormatApplier : FormatApplier<TextFormat.Number> {

    override fun onFormatSet(editText: EditText, format: TextFormat.Number) = with(editText) {
        inputType = InputType.TYPE_CLASS_NUMBER
        filters = arrayOf(
            MinMaxInputFilter(min = format.min, max = format.max)
        )
    }

    override fun onFormatUnset(editText: EditText) {}
}