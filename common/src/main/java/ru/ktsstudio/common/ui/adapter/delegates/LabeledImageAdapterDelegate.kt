package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_labeled_image_list.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 16.10.2020.
 */
class LabeledImageAdapterDelegate :
    AbsListItemAdapterDelegate<LabeledImageListItem, Any, LabeledImageAdapterDelegate.Holder>() {

    private val imagesViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            containerView = parent.inflate(R.layout.item_labeled_image_list),
            imagesViewPool = imagesViewPool
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledImageListItem
    }

    override fun onBindViewHolder(
        item: LabeledImageListItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        imagesViewPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val imagePreviewsAdapter = ImagePreviewAdapter()

        init {
            images.adapter = imagePreviewsAdapter
            images.setHasFixedSize(true)
            images.setRecycledViewPool(imagesViewPool)
        }

        fun bind(item: LabeledImageListItem) {
            label.text = item.label
            imagePreviewsAdapter.submitList(item.photoUrls)
        }
    }
}