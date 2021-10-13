package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.create_measurement.MeasurementMediaUi
import ru.ktsstudio.utilities.extensions.inflate

internal class AddMediaDelegate(
    private val onClick: (MeasurementMediaCategory) -> Unit
) : AbsListItemAdapterDelegate<
    MeasurementMediaUi.AddItem,
    Any,
    AddMediaDelegate.ViewHolder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_add_media)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MeasurementMediaUi.AddItem
    }

    override fun onBindViewHolder(
        listItem: MeasurementMediaUi.AddItem,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(listItem, onClick)
    }

    class ViewHolder(
        override val containerView: View,
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var category: MeasurementMediaCategory? = null
        private var onClick: ((MeasurementMediaCategory) -> Unit)? = null

        init {
            containerView.setOnClickListener {
                category?.let { onClick?.invoke(it) }
            }
        }

        fun bind(
            addMedia: MeasurementMediaUi.AddItem,
            onClick: (MeasurementMediaCategory) -> Unit
        ) {
            category = addMedia.category
            this.onClick = onClick
        }
    }
}
