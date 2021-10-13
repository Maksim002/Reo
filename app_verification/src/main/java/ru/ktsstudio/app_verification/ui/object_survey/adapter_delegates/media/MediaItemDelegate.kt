package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemMediaBinding
import ru.ktsstudio.app_verification.presentation.object_survey.common.SurveyMediaUi
import ru.ktsstudio.common.utils.glide.ImageLoadingDelegate
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.core_data_verfication_api.data.model.Media

internal class MediaItemDelegate(
    private val onDeleteClick: (Media) -> Unit
) : AbsListItemAdapterDelegate<
    SurveyMediaUi.LoadingMedia,
    Any,
    MediaItemDelegate.ViewHolder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            binding = parent.inflate(ItemMediaBinding::inflate)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is SurveyMediaUi.LoadingMedia
    }

    override fun onBindViewHolder(
        listItem: SurveyMediaUi.LoadingMedia,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(listItem, onDeleteClick)
    }

    class ViewHolder(
        val binding: ItemMediaBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var mediaItem: Media? = null

        private var onDeleteClick: ((Media) -> Unit)? = null

        init {
            binding.removeMedia.setOnClickListener {
                mediaItem?.let {
                    onDeleteClick?.invoke(it)
                }
            }
        }

        fun bind(
            mediaItemUi: SurveyMediaUi.LoadingMedia,
            onDeleteClick: (Media) -> Unit
        ) = with(binding) {
            mediaItem = mediaItemUi.media
            this@ViewHolder.onDeleteClick = onDeleteClick
            val remoteUrl = mediaItemUi.media.remoteUrl
            val cachedFile = mediaItemUi.media.cachedFile
            loading.isVisible = mediaItemUi.isLoading
            if (remoteUrl == null && cachedFile == null) {
                media.setImageDrawable(null)
            } else {
                ImageLoadingDelegate(media).loadImageIntoView(url = remoteUrl, file = cachedFile)
            }
        }
    }
}
