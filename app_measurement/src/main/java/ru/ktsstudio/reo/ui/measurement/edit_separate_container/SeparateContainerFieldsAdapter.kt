package ru.ktsstudio.reo.ui.measurement.edit_separate_container

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.medium.MediumTitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleAdapterDelegate
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField
import ru.ktsstudio.reo.presentation.measurement.edit_separate_container.WasteTypeUiItem
import ru.ktsstudio.reo.ui.measurement.edit_mixed_container.adapter.OpenFieldAdapterDelegate

/**
 * Created by Igor Park on 25/10/2020.
 */
class SeparateContainerFieldsAdapter(
    onTextChanged: (ContainerDataType, String) -> Unit,
    onFocusChanged: (ContainerDataType, Boolean) -> Unit,
    onWasteTypeClick: (String) -> Unit,
    onAddWasteTypeClick: (Any) -> Unit,
    onRetry: () -> Unit
) : BaseListAdapter(ContainerFieldDiffCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            LabeledValueAdapterDelegate(),
            MediumTitleAdapterDelegate(),
            OpenFieldAdapterDelegate(onTextChanged, onFocusChanged),
            SmallTitleAdapterDelegate(),
            WasteTypeAdapterDelegate(onWasteTypeClick),
            AddEntityAdapterDelegate(onAddWasteTypeClick)
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    private class ContainerFieldDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is LabeledValueItem -> oldItem.label == (newItem as LabeledValueItem).label
                is DataField -> (newItem as DataField).dataType == oldItem.dataType
                is WasteTypeUiItem -> (newItem as WasteTypeUiItem).id == oldItem.id
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
