package ru.ktsstudio.app_verification.presentation.object_filter

import ru.ktsstudio.common.presentation.filter.UiFilterItem

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
sealed class ObjectFilterUiItem : UiFilterItem {

    data class Status(
        override val id: String,
        override val title: String,
        override val isSelected: Boolean
    ) : ObjectFilterUiItem()

    data class WasteManagementType(
        override val id: String,
        override val title: String,
        override val isSelected: Boolean
    ) : ObjectFilterUiItem()

    data class Region(
        override val id: String,
        override val title: String,
        override val isSelected: Boolean
    ) : ObjectFilterUiItem()

    data class SurveyStatus(
        override val id: String,
        override val title: String,
        override val isSelected: Boolean
    ) : ObjectFilterUiItem()
}
