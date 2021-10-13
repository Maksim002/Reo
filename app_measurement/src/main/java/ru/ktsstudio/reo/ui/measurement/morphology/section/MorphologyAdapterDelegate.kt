package ru.ktsstudio.reo.ui.measurement.morphology.section

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_waste_type.*
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.R
import ru.ktsstudio.utilities.extensions.inflate

internal class MorphologyAdapterDelegate(
    private val onClick: (Long) -> Unit
) : AbsListItemAdapterDelegate<
    MorphologyItem,
    Any,
    MorphologyAdapterDelegate.Holder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            parent.inflate(R.layout.item_waste_type),
            onClick
        )
    }

    override fun onBindViewHolder(
        item: MorphologyItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MorphologyItem
    }

    class Holder(
        override val containerView: View,
        onClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var categoryId: Long? = null

        init {
            containerView.setOnClickListener {
                categoryId?.let(onClick)
            }
        }

        fun bind(item: MorphologyItem) {
            categoryId = item.localId
            wasteGroup.text = item.group.name
            wasteSubgroup.text = item.subgroup?.name
            wasteSubgroup.isVisible = item.subgroup
                ?.name
                .isNullOrBlank()
                .not()
        }
    }
}
