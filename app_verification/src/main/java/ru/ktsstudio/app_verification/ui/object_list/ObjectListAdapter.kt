package ru.ktsstudio.app_verification.ui.object_list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.app_verification.presentation.object_list.ObjectListItemUi
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.utils.isClassEqualTo

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.11.2020.
 */
class ObjectListAdapter(
    onRetry: () -> Unit,
    onClick: (objectId: String) -> Unit
) : BaseListAdapter(DiffUtilCallback()) {

    init {
        delegatesManager.withDefaultDelegates(onRetry)
            .addDelegate(ObjectAdapterDelegate(onClick))
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is ObjectListItemUi -> oldItem.id == (newItem as ObjectListItemUi).id
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}
