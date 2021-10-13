package ru.ktsstudio.common.utils.text_format.filters

import android.text.InputFilter
import android.text.Spanned

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
class TimeInputFilter : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source == null || source.isEmpty()) return null

        val char = source[0]
        val allowEdit = when (dstart) {
            0 -> char in '0'..'2'
            1 -> {
                val hourFirstDigit = dest?.get(0)
                hourFirstDigit in '0'..'1' && char in '0'..'9' || hourFirstDigit == '2' && char in '0'..'3'
            }
            2 -> char == ':'
            3 -> char in '0'..'5'
            4 -> char in '0'..'9'
            else -> false
        }

        return if (allowEdit) null else ""
    }
}