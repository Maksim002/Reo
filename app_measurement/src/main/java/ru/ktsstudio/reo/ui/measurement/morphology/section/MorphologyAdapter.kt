package ru.ktsstudio.reo.ui.measurement.morphology.section

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.EmptyListAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListErrorAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.PageLoadingAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleAdapterDelegate
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField

/**
 * Created by Igor Park on 25/10/2020.
 */
class MorphologyAdapter(
    onCategoryClick: (Long) -> Unit,
    onAddCategoryClick: (Any) -> Unit,
    onRetry: () -> Unit
) : BaseListAdapter(ContainerFieldDiffCallback()) {

    init {
        sequenceOf(
            PageLoadingAdapterDelegate(),
            EmptyListAdapterDelegate(),
            ListErrorAdapterDelegate(onRetry),
            TitleAdapterDelegate(),
            SmallTitleAdapterDelegate(),
            MorphologyAdapterDelegate(
                onCategoryClick
            ),
            AddEntityAdapterDelegate(onAddCategoryClick)
        ).forEach { delegatesManager.addDelegate(it) }
    }

    private class ContainerFieldDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is MorphologyItem -> (newItem as MorphologyItem).localId == oldItem.localId
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
