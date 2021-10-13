package ru.ktsstudio.reo.presentation.measurement_filter

import ru.ktsstudio.common.presentation.filter.UiFilterItem

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
sealed class MeasurementFilterUiItem : UiFilterItem {

    data class Mno(
        override val id: String,
        override val title: String,
        val category: String,
        val address: String,
        override val isSelected: Boolean,
    ) : MeasurementFilterUiItem()
}
