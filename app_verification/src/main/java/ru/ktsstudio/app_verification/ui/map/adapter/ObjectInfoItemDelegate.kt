package ru.ktsstudio.app_verification.ui.map.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_object_info.*
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.presentation.map.ObjectUiInfo
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
internal class ObjectInfoItemDelegate(
    private val openObjectInspection: (String) -> Unit,
    private val openMapWithRoute: (GpsPoint) -> Unit
) : AbsListItemAdapterDelegate<ObjectUiInfo, Any, ObjectInfoItemDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            containerView = parent.inflate(R.layout.item_object_info),
            openObjectInspection = openObjectInspection,
            openMapWithRoute = openMapWithRoute
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ObjectUiInfo
    }

    override fun onBindViewHolder(listItem: ObjectUiInfo, viewHolder: ViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(listItem)
    }

    class ViewHolder(
        override val containerView: View,
        openObjectInspection: (String) -> Unit,
        openMapWithRoute: (GpsPoint) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var verificationObject: ObjectUiInfo? = null

        init {
            openDetails.setOnClickListener { verificationObject?.id?.let(openObjectInspection) }
            route.setOnClickListener { verificationObject?.location?.let(openMapWithRoute) }
        }

        fun bind(listItem: ObjectUiInfo) {
            verificationObject = listItem
            sourceName.text = listItem.name
            address.text = listItem.address
            date.text = listItem.date
            status.text = listItem.status
            type.text = listItem.type
        }
    }
}
