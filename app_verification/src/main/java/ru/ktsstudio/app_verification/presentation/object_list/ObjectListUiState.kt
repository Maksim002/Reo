package ru.ktsstudio.app_verification.presentation.object_list

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
data class ObjectListUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<ObjectListItemUi>,
    val searchQuery: String,
    val isFilterSet: Boolean
)
