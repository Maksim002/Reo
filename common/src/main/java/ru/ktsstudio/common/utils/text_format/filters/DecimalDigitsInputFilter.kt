package ru.ktsstudio.common.utils.text_format.filters

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(digitsAfterZero: Int) : InputFilter {

    private val regexPattern = Pattern.compile("[0-9]+((\\.[0-9]{0,$digitsAfterZero})?)")

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = StringBuilder(dest.toString())
            .apply { insert(dstart, source) }
            .toString()
            .let(regexPattern::matcher)
        return "".takeIf { matcher.matches().not() }
    }
}