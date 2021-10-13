package ru.ktsstudio.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.view_toolbar_action.view.*
import permissions.dispatcher.PermissionRequest
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.common.utils.text_format.applier.FormatApplier
import ru.ktsstudio.common.utils.text_format.applier.NumberDecimalFormatApplier
import ru.ktsstudio.common.utils.text_format.applier.NumberFormatApplier
import ru.ktsstudio.common.utils.text_format.applier.TextFormatApplier
import ru.ktsstudio.common.utils.text_format.applier.TimeFormatApplier
import ru.ktsstudio.utilities.extensions.setColor
import ru.ktsstudio.utilities.extensions.string
import ru.ktsstudio.utilities.extensions.updatePadding

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */

fun View.updateBackground(@DrawableRes originalRes: Int? = null, update: (Drawable) -> Unit) {
    background = (originalRes?.let {
        ContextCompat.getDrawable(context, it)
    } ?: background)
        ?.mutate()
        ?.apply(update)
}

fun Context.showRationaleDialog(
    @StringRes dialogMessage: Int,
    request: PermissionRequest
): AlertDialog {
    return AlertDialog.Builder(this)
        .setMessage(dialogMessage)
        .setPositiveButton(
            R.string.dialog_yes,
            { _, _ -> request.proceed() }
        )
        .setNegativeButton(
            R.string.dialog_no,
            { _, _ -> request.cancel() }
        )
        .show()
}

fun Context.showRationaleDialog(
    dialogMessage: String,
    request: PermissionRequest
): AlertDialog {
    return AlertDialog.Builder(this)
        .setMessage(dialogMessage)
        .setPositiveButton(
            R.string.dialog_yes,
            { _, _ -> request.proceed() }
        )
        .setNegativeButton(
            R.string.dialog_no,
            { _, _ -> request.cancel() }
        )
        .show()
}

fun View.setBackgroundLayerTint(@ColorRes color: Int, @IdRes layerId: Int) {
    val mutatedBackground = background.mutate()
    (mutatedBackground as LayerDrawable).findDrawableByLayerId(layerId)
        .setColor(context, color)
    background = mutatedBackground
}

fun View.updateDrawableLayer(@IdRes layerId: Int, updateFun: (Drawable) -> Unit) {
    val mutatedBackground = background.mutate()
    (mutatedBackground as LayerDrawable).findDrawableByLayerId(layerId)
        .also { updateFun(it) }
    background = mutatedBackground
}

fun Drawable.setStroke(@Px strokeWidth: Int, @ColorInt color: Int) {
    (this as GradientDrawable).setStroke(strokeWidth, color)
}

fun EditText.setValueWithoutEventTrigger(value: String?, watcher: TextWatcher) {
    val textDiffers = value != string()
    val isTyping = hasFocus()
    if (textDiffers && isTyping.not()) {
        removeTextChangedListener(watcher)
        val cursorPosition = selectionStart
        setText(value.orEmpty())
        setSelection(cursorPosition.coerceAtMost(value.orEmpty().length))
        addTextChangedListener(watcher)
    }
}

fun RadioGroup.setValueWithoutEventTrigger(checkedButtonId: Int, onCheckChangeListener: RadioGroup.OnCheckedChangeListener) {
    if (checkedButtonId != checkedRadioButtonId) {
        setOnCheckedChangeListener(null)
        check(checkedButtonId)
        setOnCheckedChangeListener(onCheckChangeListener)
    }
}

fun CheckBox.setCheckedWithoutEventTrigger(checked: Boolean, listener: CompoundButton.OnCheckedChangeListener) {
    if (isChecked != checked) {
        setOnCheckedChangeListener(null)
        isChecked = checked
        setOnCheckedChangeListener(listener)
    }
}

fun MenuItem.setupCustomLayoutParams(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    @ColorInt textColor: Int,
    onClick: () -> Unit
) {
    actionView.apply {
        actionIcon.setImageResource(icon)
        actionText.setText(text)
        actionText.setTextColor(textColor)
        actionContainer.setOnClickListener { onClick() }
    }
}

fun EditText.updateInputTypeFormat(format: TextFormat) {
    val formatToApplierMap = mapOf(
        TextFormat.Text::class to TextFormatApplier(),
        TextFormat.Number::class to NumberFormatApplier(),
        TextFormat.Time::class to TimeFormatApplier(),
        TextFormat.NumberDecimal::class to NumberDecimalFormatApplier()
    )

    formatToApplierMap.values.forEach {
        it.onFormatUnset(this)
    }
    formatToApplierMap[format::class]?.let {
        it as FormatApplier<TextFormat>
    }?.onFormatSet(this, format)
}

fun <T : ViewBinding> ViewGroup.inflate(
    inflateBinding: (
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean
    ) -> T, attachToRoot: Boolean = false
): T {
    val inflater = LayoutInflater.from(context)
    return inflateBinding(inflater, this, attachToRoot)
}

fun View.setHorizontalPaddingDimen(@DimenRes padding: Int) {
    val paddingPx = resources.getDimensionPixelSize(padding)
    setHorizontalPaddingPx(paddingPx)
}

fun View.setHorizontalPaddingPx(@Px padding: Int) {
    updatePadding(left = padding, right = padding)
}

fun View.setupInCard(inCard: Boolean) {
    if (inCard) {
        setHorizontalPaddingDimen(R.dimen.default_double_padding)
        setBackgroundResource(R.drawable.bg_card_sides)
    } else {
        setHorizontalPaddingPx(0)
        background = null
    }
}