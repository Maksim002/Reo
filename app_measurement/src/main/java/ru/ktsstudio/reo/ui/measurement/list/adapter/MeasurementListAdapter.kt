package ru.ktsstudio.reo.ui.measurement.list.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.list.MeasurementListItemUi

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListAdapter(
    onRetry: () -> Unit,
    onClick: (measurementId: Long) -> Unit
) : BaseListAdapter(MeasurementListItemDiffCalculator()) {

    init {
        delegatesManager.withDefaultDelegates(onRetry)
            .addDelegate(MeasurementItemDelegate(onClick))
    }

    override fun getEmptyListItem(): ListStateItem.EmptyList? {
        return ListStateItem.EmptyList(
            title = R.string.measurement_list_empty_title,
            message = R.string.default_empty_message
        )
    }

    private class MeasurementListItemDiffCalculator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is MeasurementListItemUi -> oldItem.id == (newItem as MeasurementListItemUi).id
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? = Unit
    }
}
