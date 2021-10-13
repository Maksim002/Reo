package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemLabeledMediaListBinding
import ru.ktsstudio.app_verification.presentation.object_survey.common.SurveyMediaUi
import ru.ktsstudio.app_verification.presentation.object_survey.common.SurveyMediaUi.LoadingMedia
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.Identifier
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setupInCard
import ru.ktsstudio.core_data_verfication_api.data.model.Media

/**
 * Created by Igor Park on 16/11/2020.
 */
class LabeledMediaListItemAdapterDelegate(
    private val onAddClick: (ValueConsumer<List<Media>, *>) -> Unit,
    private val onDeleteClick: (Pair<ValueConsumer<List<Media>, *>, Media>) -> Unit
) : AbsListItemAdapterDelegate<LabeledMediaListItem, Any, LabeledMediaListItemAdapterDelegate.Holder>() {

    private val mediaViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLabeledMediaListBinding::inflate),
            imagesViewPool = mediaViewPool,
            onAddClick = onAddClick,
            onDeleteClick = onDeleteClick
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledMediaListItem
    }

    override fun onBindViewHolder(
        item: LabeledMediaListItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        val binding: ItemLabeledMediaListBinding,
        imagesViewPool: RecyclerView.RecycledViewPool,
        onAddClick: (ValueConsumer<List<Media>, *>) -> Unit,
        onDeleteClick: (Pair<ValueConsumer<List<Media>, *>, Media>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var valueConsumer: ValueConsumer<List<Media>, *>? = null
        private val imagePreviewsAdapter = MediaListAdapter(
            onDeleteClick = { media ->
                valueConsumer?.let {
                    onDeleteClick(it to media)
                }
            },
            onAddClick = { valueConsumer?.let(onAddClick) }
        )

        init {
            with(binding.medias) {
                adapter = imagePreviewsAdapter
                setRecycledViewPool(imagesViewPool)
                layoutManager = FlexboxLayoutManager(context)
                    .apply {
                        flexWrap = FlexWrap.WRAP
                        flexDirection = FlexDirection.ROW
                    }
            }
        }

        fun bind(item: LabeledMediaListItem) = with(binding) {
            root.isActivated = item.isNested
            container.setupInCard(item.inCard)
            label.text = item.label
            setNewPhotos(item.identifier, item.valueConsumer)
        }

        private fun setNewPhotos(
            identifier: Identifier,
            valueConsumer: ValueConsumer<List<Media>, *>
        ) {
            this.valueConsumer = valueConsumer
            imagePreviewsAdapter.items = listOf(SurveyMediaUi.AddItem(identifier)) +
                valueConsumer.get().map(::LoadingMedia)
        }
    }
}
