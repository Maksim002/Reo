package ru.ktsstudio.common.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.ktsstudio.common.R
import ru.ktsstudio.common.ui.adapter.delegates.EmptyListAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListErrorAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.PageLoadingAdapterDelegate

/**
 * Created by Igor Park on 24/03/2020.
 */
open class BaseListAdapter(
    diffCallback: DiffUtil.ItemCallback<Any>
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    protected open fun getEmptyListItem(): ListStateItem.EmptyList? {
        return null
    }

    fun setDataWithState(
        data: List<Any>,
        isNextPageLoading: Boolean,
        isNextPageError: Boolean,
        isPrevPageLoading: Boolean,
        isPrevPageError: Boolean,
        isLoading: Boolean,
        error: Throwable?
    ) {
        val listToSet = when {
            isLoading -> listOf(ListStateItem.LoadingPage(isListLoading = true))
            error != null -> listOf(ListStateItem.ErrorList(error))
            isPrevPageLoading -> listOf(ListStateItem.LoadingPage(isListLoading = false)) + data
            isNextPageLoading -> data + listOf(ListStateItem.LoadingPage(isListLoading = false))
            isPrevPageError -> listOf(ListStateItem.ErrorPage) + data
            isNextPageError -> data + listOf(ListStateItem.ErrorPage)
            data.isEmpty() -> listOf(
                getEmptyListItem() ?: ListStateItem.EmptyList(
                    title = R.string.default_empty_title,
                    message = R.string.default_empty_message
                )
            )
            else -> data
        }

        items = listToSet
    }

    protected fun AdapterDelegatesManager<List<Any>>.withDefaultDelegates(
        onRetry: () -> Unit
    ): AdapterDelegatesManager<List<Any>> {
        return addDelegate(PageLoadingAdapterDelegate())
            .addDelegate(EmptyListAdapterDelegate())
            .addDelegate(ListErrorAdapterDelegate(onRetry))
    }
}
