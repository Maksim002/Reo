package ru.ktsstudio.feature_mno_list.ui.list.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.EmptyListAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListErrorAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.PageLoadingAdapterDelegate
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.presentation.list.MnoListItemUi

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal class MnoListAdapter(
    onRetry: () -> Unit,
    onClick: (mnoId: String) -> Unit
) : BaseListAdapter(MnoListItemDiffCalculator()) {

    init {
        delegatesManager.withDefaultDelegates(onRetry)
            .addDelegate(MnoItemDelegate(onClick))
    }

    override fun getEmptyListItem(): ListStateItem.EmptyList? {
        return ListStateItem.EmptyList(
            title = R.string.mno_list_empty_title,
            message = R.string.default_empty_message
        )
    }

    private class MnoListItemDiffCalculator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is MnoListItemUi -> oldItem.id == (newItem as MnoListItemUi).id
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
    }
}