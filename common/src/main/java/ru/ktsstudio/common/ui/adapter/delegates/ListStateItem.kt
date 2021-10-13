package ru.ktsstudio.common.ui.adapter.delegates

import androidx.annotation.StringRes

sealed class ListStateItem {
    data class LoadingPage(val isListLoading: Boolean) : ListStateItem()
    object ErrorPage : ListStateItem()

    data class ErrorList(
        val error: Throwable
    ) : ListStateItem()

    data class EmptyList(
        @StringRes val title: Int,
        @StringRes val message: Int
    ) : ListStateItem()
}
