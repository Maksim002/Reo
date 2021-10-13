package ru.ktsstudio.reo.ui.measurement.edit_separate_container

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_waste_type.*
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.edit_separate_container.WasteTypeUiItem
import ru.ktsstudio.utilities.extensions.inflate

internal class WasteTypeAdapterDelegate(
    private val onClick: (String) -> Unit
) : AbsListItemAdapterDelegate<
    WasteTypeUiItem,
    Any,
    WasteTypeAdapterDelegate.Holder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_waste_type), onClick)
    }

    override fun onBindViewHolder(
        item: WasteTypeUiItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is WasteTypeUiItem
    }

    class Holder(
        override val containerView: View,
        onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var wasteTypeId: String? = null

        init {
            containerView.setOnClickListener {
                wasteTypeId?.let(onClick)
            }
        }

        fun bind(item: WasteTypeUiItem) = with(item) {
            wasteTypeId = item.id
            wasteGroup.text = categoryName
            wasteSubgroup.text = otherCategoryName
            wasteSubgroup.isVisible = otherCategoryName.isNullOrBlank().not()
        }
    }
}
