package ru.ktsstudio.reo.presentation.mno_filter

import ru.ktsstudio.common.presentation.filter.UiFilterItem

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
sealed class MnoFilterUiItem : UiFilterItem {

    data class MnoCategory(
        override val id: String,
        override val title: String,
        override val isSelected: Boolean
    ) : MnoFilterUiItem()
}
