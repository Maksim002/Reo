package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement_media_list.*
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 16/11/2020.
 */
class MeasurementMediaAdapter(
    private val onDeleteClick: (Long) -> Unit,
    private val onAddClick: (MeasurementMediaCategory) -> Unit
) : AbsListItemAdapterDelegate<MeasurementMediaListItem, Any, MeasurementMediaAdapter.Holder>() {

    private val mediaViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            containerView = parent.inflate(R.layout.item_measurement_media_list),
            imagesViewPool = mediaViewPool,
            onAddClick = onAddClick,
            onDeleteClick = onDeleteClick
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MeasurementMediaListItem
    }

    override fun onBindViewHolder(
        item: MeasurementMediaListItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        imagesViewPool: RecyclerView.RecycledViewPool,
        onDeleteClick: (Long) -> Unit,
        onAddClick: (MeasurementMediaCategory) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var mediaCategory: MeasurementMediaCategory? = null

        private val imagePreviewsAdapter = MediaListAdapter(onDeleteClick, onAddClick)

        init {
            with(medias) {
                adapter = imagePreviewsAdapter
                setRecycledViewPool(imagesViewPool)
                layoutManager = FlexboxLayoutManager(context)
                    .apply {
                        flexWrap = FlexWrap.WRAP
                        flexDirection = FlexDirection.ROW
                    }
            }
        }

        fun bind(item: MeasurementMediaListItem) {
            mediaCategory = item.category
            title.text = item.title
            imagePreviewsAdapter.items = item.mediaList
        }
    }
}
