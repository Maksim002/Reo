package ru.ktsstudio.reo.ui.measurement.edit_waste_type

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.medium.MediumTitleAdapterDelegate
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField
import ru.ktsstudio.reo.ui.measurement.edit_mixed_container.adapter.OpenFieldAdapterDelegate

/**
 * Created by Igor Park on 25/10/2020.
 */
class WasteTypeFieldsAdapter(
    onTextChanged: (ContainerDataType, String) -> Unit,
    onFocusChanged: (ContainerDataType, Boolean) -> Unit,
    onRetry: () -> Unit
) : BaseListAdapter(WasteTypeFieldDiffCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            SelectorAdapterDelegate(onTextChanged),
            OpenFieldAdapterDelegate(onTextChanged, onFocusChanged),
            MediumTitleAdapterDelegate()
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    override fun getEmptyListItem(): ListStateItem.EmptyList? {
        return ListStateItem.EmptyList(
            title = R.string.edit_morphology_waste_categories_empty_title,
            message = R.string.default_empty_message
        )
    }

    private class WasteTypeFieldDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is DataField -> (newItem as DataField).dataType == oldItem.dataType
                else -> oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return when (newItem) {
                is ContainerField.OpenField,
                is ContainerField.Selector -> newItem
                else -> null
            }
        }
    }
}
