package ru.ktsstudio.common.utils.text_format.applier

import android.text.InputType
import android.widget.EditText
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.common.utils.text_format.filters.DecimalDigitsInputFilter
import ru.ktsstudio.common.utils.text_format.filters.MinMaxInputFilter

/**
 * @author Maxim Ovchinnikov on 04.12.2020.
 */
class NumberDecimalFormatApplier : FormatApplier<TextFormat.NumberDecimal> {

    override fun onFormatSet(editText: EditText, format: TextFormat.NumberDecimal) = with(editText) {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        filters = arrayOf(
            DecimalDigitsInputFilter(digitsAfterZero = format.decimalDigits),
            MinMaxInputFilter(min = format.min, max = format.max)
        )
    }

    override fun onFormatUnset(editText: EditText) {}
}