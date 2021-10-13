package ru.ktsstudio.reo.presentation.measurement.edit_waste_type

import ru.ktsstudio.common.presentation.filter.UiFilterItem

data class WasteGroupUi(
    override val id: String,
    override val title: String,
    override val isSelected: Boolean
) : UiFilterItem
