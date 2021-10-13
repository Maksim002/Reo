package ru.ktsstudio.app_verification.ui.map.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.presentation.map.ObjectUiInfo
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.DividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.utils.isClassEqualTo

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
internal class ObjectInfoAdapter(
    onRetry: () -> Unit,
    openObjectInspection: (String) -> Unit,
    openMapWithRoute: (GpsPoint) -> Unit
) : BaseListAdapter(ObjectListItemDiffCalculator()) {

    init {
        delegatesManager.withDefaultDelegates(onRetry)
            .addDelegate(ObjectInfoItemDelegate(openObjectInspection, openMapWithRoute))
            .addDelegate(DividerAdapterDelegate())
    }

    override fun getEmptyListItem(): ListStateItem.EmptyList? {
        return ListStateItem.EmptyList(
            title = R.string.object_info_empty_title,
            message = R.string.default_not_found_message
        )
    }

    private class ObjectListItemDiffCalculator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is ObjectUiInfo -> oldItem.id == (newItem as ObjectUiInfo).id
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
    }
}
