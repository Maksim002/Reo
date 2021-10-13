package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.presentation.measurement.create_measurement.MeasurementMediaUi

internal class MediaListAdapter(
    onDeleteClick: (Long) -> Unit,
    onAddClick: (MeasurementMediaCategory) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(MediaListItemDiffCallback()) {

    init {
        sequenceOf(
            MediaItemDelegate(onDeleteClick),
            AddMediaDelegate(onAddClick)
        ).forEach { delegatesManager.addDelegate(it) }
    }

    private class MediaListItemDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is MeasurementMediaUi.Media -> oldItem.media.id == (newItem as MeasurementMediaUi.Media).media.id
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? = Unit
    }
}
