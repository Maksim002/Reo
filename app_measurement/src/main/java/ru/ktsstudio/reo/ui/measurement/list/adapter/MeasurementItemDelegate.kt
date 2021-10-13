package ru.ktsstudio.reo.ui.measurement.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement.*
import ru.ktsstudio.common.utils.setStroke
import ru.ktsstudio.common.utils.updateDrawableLayer
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.list.MeasurementListItemUi
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementItemDelegate(
    private val onClick: (measurementId: Long) -> Unit
) : AbsListItemAdapterDelegate<MeasurementListItemUi, Any, MeasurementItemDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            containerView = parent.inflate(R.layout.item_measurement),
            onClick = onClick
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MeasurementListItemUi
    }

    override fun onBindViewHolder(
        listItem: MeasurementListItemUi,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(listItem)
    }

    class ViewHolder(
        override val containerView: View,
        onClick: (measurementId: Long) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var currentItemId: Long? = null

        init {
            containerView.setOnClickListener {
                currentItemId?.let(onClick)
            }
        }

        fun bind(listItem: MeasurementListItemUi) {
            currentItemId = listItem.id
            val measurementAvailabilityText = containerView.context.getString(
                getMeasurementAvailabilityTextResource(listItem.measurementAvailability)
            )
            val measurementStatusName = listItem.measurementStatus.name

            source.text = listItem.sourceName
            category.text = listItem.categoryName
            measurementAvailability.text = measurementAvailabilityText
            address.text = listItem.address
            measurementCreatedDate.text = listItem.measurementCreatedDate
            measurementStatus.text = measurementStatusName
            linearLayout.updateDrawableLayer(R.id.strokeLayer) {
                it.setStroke(
                    strokeWidth = itemView.resources.getDimensionPixelSize(R.dimen.strokeWidth),
                    color = ContextCompat.getColor(itemView.context, listItem.statusColor)
                )
            }
        }

        private fun getMeasurementAvailabilityTextResource(measurementAvailability: Boolean): Int {
            return if (measurementAvailability) {
                R.string.measurement_item_available
            } else {
                R.string.measurement_item_not_available
            }
        }
    }
}
