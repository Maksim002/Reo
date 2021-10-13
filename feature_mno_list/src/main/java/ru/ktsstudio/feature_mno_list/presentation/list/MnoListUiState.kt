package ru.ktsstudio.feature_mno_list.presentation.list

/**
 * @author Maxim Myalkin (MaxMyalkin) on 08.10.2020.
 */
internal data class MnoListUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<MnoListItemUi>,
    val searchQuery: String,
    val isFilterSet: Boolean,
)