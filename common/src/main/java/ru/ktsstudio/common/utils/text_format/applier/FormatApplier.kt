package ru.ktsstudio.common.utils.text_format.applier

import android.widget.EditText
import ru.ktsstudio.common.utils.text_format.TextFormat

/**
 * @author Maxim Ovchinnikov on 04.12.2020.
 */
interface FormatApplier<T : TextFormat> {
    fun onFormatSet(editText: EditText, format: T)
    fun onFormatUnset(editText: EditText)
}
