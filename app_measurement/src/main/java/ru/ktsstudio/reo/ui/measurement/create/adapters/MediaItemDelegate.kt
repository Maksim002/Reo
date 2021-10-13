package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement_media.*
import ru.ktsstudio.common.utils.glide.ImageLoadingDelegate
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement.create_measurement.MeasurementMediaUi
import ru.ktsstudio.utilities.extensions.inflate

internal class MediaItemDelegate(
    private val onDeleteClick: (Long) -> Unit
) : AbsListItemAdapterDelegate<
    MeasurementMediaUi.Media,
    Any,
    MediaItemDelegate.ViewHolder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_measurement_media)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MeasurementMediaUi.Media
    }

    override fun onBindViewHolder(
        listItem: MeasurementMediaUi.Media,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(listItem, onDeleteClick)
    }

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var mediaLocalId: Long? = null
        private var onDeleteClick: ((Long) -> Unit)? = null

        init {
            removeMedia.setOnClickListener {
                mediaLocalId?.let { onDeleteClick?.invoke(it) }
            }
        }

        fun bind(
            measurementMedia: MeasurementMediaUi.Media,
            onDeleteClick: (Long) -> Unit
        ) {
            mediaLocalId = measurementMedia.media.id
            this.onDeleteClick = onDeleteClick
            imageLoading.isVisible = measurementMedia.isLoading
            val remoteUrl = measurementMedia.media.remoteUrl
            val cachedFile = measurementMedia.media.cachedFile
            when {
                cachedFile != null && cachedFile.exists() -> {
                    ImageLoadingDelegate(media).loadImageIntoView(file = cachedFile)
                }
                remoteUrl != null -> {
                    ImageLoadingDelegate(media).loadImageIntoView(url = remoteUrl)
                }
            }
        }
    }
}
