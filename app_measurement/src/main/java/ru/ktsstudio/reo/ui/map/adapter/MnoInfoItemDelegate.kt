package ru.ktsstudio.reo.ui.map.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_mno_info.*
import ru.ktsstudio.common.utils.resourcesLocale
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.map.MnoUiInfo
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
internal class MnoInfoItemDelegate(
    private val openObjectDetails: (String) -> Unit
) : AbsListItemAdapterDelegate<MnoUiInfo, Any, MnoInfoItemDelegate.ViewHolder>() {

    private val containersViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            containerView = parent.inflate(R.layout.view_mno_info),
            containersViewPool = containersViewPool,
            openObjectDetails = openObjectDetails
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MnoUiInfo
    }

    override fun onBindViewHolder(listItem: MnoUiInfo, viewHolder: ViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(listItem)
    }

    class ViewHolder(
        override val containerView: View,
        containersViewPool: RecyclerView.RecycledViewPool,
        openObjectDetails: (String) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var mnoId: String? = null
        private val containersAdapter = ContainersAdapter()

        init {
            containers.adapter = containersAdapter
            containers.setRecycledViewPool(containersViewPool)
            openDetails.setOnClickListener { mnoId?.let(openObjectDetails) }
        }

        fun bind(listItem: MnoUiInfo) {
            mnoId = listItem.id
            sourceName.text = listItem.sourceName
            sourceType.text = listItem.sourceType
            measurementCount.text = containerView.context
                .resourcesLocale()
                .getQuantityString(
                    R.plurals.mno_info_measurement_count,
                    listItem.measurementCount,
                    listItem.measurementCount
                )
            address.text = listItem.address
            containersAdapter.setItems(listItem.containers)
        }
    }
}
