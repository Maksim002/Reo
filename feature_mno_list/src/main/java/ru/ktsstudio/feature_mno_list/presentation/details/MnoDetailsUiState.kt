package ru.ktsstudio.feature_mno_list.presentation.details

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
data class MnoDetailsUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)