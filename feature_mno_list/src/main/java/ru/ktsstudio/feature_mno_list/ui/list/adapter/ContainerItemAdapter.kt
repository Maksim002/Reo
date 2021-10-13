package ru.ktsstudio.feature_mno_list.ui.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_container.*
import ru.ktsstudio.common.utils.resourcesLocale
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.presentation.list.ContainerItemUi
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 05.10.2020.
 */
internal class ContainerItemAdapter :
    ListAdapter<ContainerItemUi, ContainerItemAdapter.Holder>(ContainersDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_container))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: ContainerItemUi) = with(item) {
            containerName.text = name
            containerCount.text = containerView.context.resourcesLocale()
                .getString(R.string.mno_item_containers_count, count)
            containerCount.requestLayout()
        }
    }

    private class ContainersDiffCallback : DiffUtil.ItemCallback<ContainerItemUi>() {
        override fun areItemsTheSame(oldItem: ContainerItemUi, newItem: ContainerItemUi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ContainerItemUi,
            newItem: ContainerItemUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}
