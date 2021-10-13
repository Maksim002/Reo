package ru.ktsstudio.reo.ui.map.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.DividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.reo.presentation.map.MnoUiInfo

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
internal class MnoInfoAdapter(
    onRetry: () -> Unit,
    openObjectDetails: (String) -> Unit
) : BaseListAdapter(MnoListItemDiffCalculator()) {

    init {
        sequenceOf(
            DividerAdapterDelegate(),
            MnoInfoItemDelegate(openObjectDetails),
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    override fun getEmptyListItem(): ListStateItem.EmptyList? {
        return ListStateItem.EmptyList(
            title = R.string.mno_list_empty_title,
            message = R.string.default_not_found_message
        )
    }

    private class MnoListItemDiffCalculator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is MnoUiInfo -> oldItem.id == (newItem as MnoUiInfo).id
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
    }
}
