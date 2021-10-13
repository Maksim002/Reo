package ru.ktsstudio.reo.ui.measurement.edit_mixed_container.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.medium.MediumTitleAdapterDelegate
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField

/**
 * Created by Igor Park on 25/10/2020.
 */
class ContainerFieldsAdapter(
    onTextChanged: (ContainerDataType, String) -> Unit,
    onFocusChanged: (ContainerDataType, Boolean) -> Unit,
    onRetry: () -> Unit
) : BaseListAdapter(ContainerFieldDiffCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            LabeledValueAdapterDelegate(),
            MediumTitleAdapterDelegate(),
            OpenFieldAdapterDelegate(onTextChanged, onFocusChanged)
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    private class ContainerFieldDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is LabeledValueItem -> oldItem.label == (newItem as LabeledValueItem).label
                is ContainerField.Title -> newItem == oldItem
                is DataField -> (newItem as DataField).dataType == oldItem.dataType
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return when (newItem) {
                is ContainerField.OpenField -> newItem
                else -> null
            }
        }
    }
}
