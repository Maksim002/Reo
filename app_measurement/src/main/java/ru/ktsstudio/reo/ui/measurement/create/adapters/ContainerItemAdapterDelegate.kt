package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement_container.*
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.reo.R
import ru.ktsstudio.utilities.extensions.inflate

internal class ContainerItemAdapterDelegate(
    private val onClick: (Long, Boolean) -> Unit
) : AbsListItemAdapterDelegate<
    MeasurementComposite.Container,
    Any,
    ContainerItemAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_measurement_container), onClick)
    }

    override fun onBindViewHolder(
        item: MeasurementComposite.Container,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MeasurementComposite.Container
    }

    class Holder(
        override val containerView: View,
        onClick: (Long, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var container: MeasurementComposite.Container? = null

        init {
            containerView.setOnClickListener {
                container?.let {
                    onClick(it.id, it.isSeparate)
                }
            }
        }

        fun bind(item: MeasurementComposite.Container) = with(item) {
            container = item
            containerName.text = name
            containerVolume.text = containerView.resources.getString(
                R.string.volume_value,
                volume.toString()
            )
        }
    }
}
