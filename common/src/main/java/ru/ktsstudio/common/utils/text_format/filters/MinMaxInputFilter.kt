package ru.ktsstudio.common.utils.text_format.filters

import android.text.InputFilter
import android.text.Spanned

/**
 * Created by Igor Park on 25/10/2020.
 */

class MinMaxInputFilter(
    private val min: Float,
    private val max: Float
) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val numberInput = StringBuilder(dest.toString())
                .apply { insert(dstart, source) }
                .toString()
                .toFloat()

            if (numberInput in min..max) return null
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }
}