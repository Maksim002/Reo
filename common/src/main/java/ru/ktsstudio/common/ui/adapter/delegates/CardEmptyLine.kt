package ru.ktsstudio.common.ui.adapter.delegates

import androidx.annotation.Px

/**
 * Created by Igor Park on 12/11/2020.
 */
data class CardEmptyLine(
    @Px val height: Int,
    @Px val horizontalPadding: Int = 0,
    val isNested: Boolean = false
)