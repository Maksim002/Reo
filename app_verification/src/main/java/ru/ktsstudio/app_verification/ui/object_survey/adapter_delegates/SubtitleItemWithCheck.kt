package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import androidx.annotation.DimenRes
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.12.2020.
 */
data class SubtitleItemWithCheck(
    val checkableValueConsumer: CheckableValueConsumer<Unit, *>,
    val title: String,
    val isNested: Boolean = true,
    val withAccent: Boolean = true,
    @DimenRes
    val bottomPadding: Int? = R.dimen.list_item_padding
)
