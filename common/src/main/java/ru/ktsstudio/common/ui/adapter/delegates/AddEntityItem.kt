package ru.ktsstudio.common.ui.adapter.delegates

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import ru.ktsstudio.common.R

/**
 * Created by Igor Park on 31/10/2020.
 */
data class AddEntityItem<EntityQualifier>(
    @DrawableRes val icon: Int = R.drawable.ic_plus,
    @ColorRes val background: Int? = null,
    val nested: Boolean = false,
    val text: String,
    val qualifier: EntityQualifier
)