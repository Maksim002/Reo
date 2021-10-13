package ru.ktsstudio.reo.ui.measurement.add_container

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_container_name.*
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.add_container.MnoContainerUi
import ru.ktsstudio.utilities.extensions.inflate

internal class MnoContainerAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<MnoContainerUi, MnoContainerAdapter.Holder>(ContainerNamesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_container_name), onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        override val containerView: View,
        onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var mnoContainerId: String? = null

        init {
            containerView.setOnClickListener {
                mnoContainerId?.let(onClick)
            }
        }

        fun bind(item: MnoContainerUi) {
            mnoContainerId = item.mnoContainer.id

            containerName.text = item.mnoContainer.name
            containerType.text = item.mnoContainer.type.name
            checkbox.isChecked = item.isSelected
        }
    }

    private class ContainerNamesDiffCallback : DiffUtil.ItemCallback<MnoContainerUi>() {
        override fun areItemsTheSame(oldItem: MnoContainerUi, newItem: MnoContainerUi): Boolean {
            return oldItem.mnoContainer.id == newItem.mnoContainer.id
        }

        override fun areContentsTheSame(
            oldItem: MnoContainerUi,
            newItem: MnoContainerUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}
