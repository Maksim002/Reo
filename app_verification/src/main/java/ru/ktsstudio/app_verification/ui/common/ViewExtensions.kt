package ru.ktsstudio.app_verification.ui.common

import android.widget.TextView
import androidx.core.widget.TextViewCompat
import ru.ktsstudio.common.R

/**
 * Created by Igor Park on 25/01/2021.
 */
fun TextView.setParagraphSmallBold(shouldMake: Boolean) {
    val styleRes = if (shouldMake) {
        R.style.BaseTextAppearance_ParagraphSmallBold
    } else {
        R.style.BaseTextAppearance_Paragraph_Small
    }
    TextViewCompat.setTextAppearance(this, styleRes)
}
