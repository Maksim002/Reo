package ru.ktsstudio.feature_auth.utils

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * Created by Igor Park on 28/09/2020.
 */
internal fun AppCompatEditText.setupEndButton(
    icon: Drawable,
    onClick: () -> Unit
) {
    addTextChangedListener(AfterTextChangedWatcher { text ->
        val compoundDrawable = icon.takeIf { text.isNotEmpty() }
        setCompoundDrawablesWithIntrinsicBounds(null, null, compoundDrawable, null)
    })
    setOnTouchListener(View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight) &&
                text?.isNotEmpty().orFalse()
            ) {
                onClick.invoke()
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
}
