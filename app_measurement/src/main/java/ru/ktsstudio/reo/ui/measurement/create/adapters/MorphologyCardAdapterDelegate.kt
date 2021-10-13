package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_card_morphology.*
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.R
import ru.ktsstudio.utilities.extensions.inflate

class MorphologyCardAdapterDelegate : AbsListItemAdapterDelegate<
    MorphologyItem,
    Any,
    MorphologyCardAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_card_morphology))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MorphologyItem
    }

    override fun onBindViewHolder(
        item: MorphologyItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: MorphologyItem) {
            group.text = item.group.name
            subGroup.isVisible = item.subgroup != null
            subGroup.text = item.subgroup?.name
        }
    }
}
