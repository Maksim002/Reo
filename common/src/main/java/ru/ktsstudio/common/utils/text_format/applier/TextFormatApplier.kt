package ru.ktsstudio.common.utils.text_format.applier

import android.text.InputType
import android.widget.EditText
import ru.ktsstudio.common.utils.text_format.TextFormat

/**
 * @author Maxim Ovchinnikov on 04.12.2020.
 */
class TextFormatApplier : FormatApplier<TextFormat.Text> {

    override fun onFormatSet(editText: EditText, format: TextFormat.Text) = with(editText) {
        inputType = InputType.TYPE_CLASS_TEXT
        filters = emptyArray()
    }

    override fun onFormatUnset(editText: EditText) {}
}
