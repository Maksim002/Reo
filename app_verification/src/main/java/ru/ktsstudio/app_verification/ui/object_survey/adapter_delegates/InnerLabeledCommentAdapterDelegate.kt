package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerLabeledCommentBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.setupInCard
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
class InnerLabeledCommentAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<InnerLabeledComment, Any, InnerLabeledCommentAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerLabeledCommentBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerLabeledComment
    }

    override fun onBindViewHolder(
        item: InnerLabeledComment,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as InnerLabeledComment).valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemInnerLabeledCommentBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var valueConsumer: ValueConsumer<String?, *>? = null
        private val textListener = AfterTextChangedWatcher { newText ->
            valueConsumer?.consume(newText)
                ?.let(onDataChanged)
        }

        init {
            binding.commentInput.addTextChangedListener(textListener)
        }

        fun bind(item: InnerLabeledComment) = with(binding) {
            root.isActivated = true
            container.setupInCard(item.inCard)
            commentTitle.text = item.label
            commentInput.hint = item.editHint
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: ValueConsumer<String?, *>) {
            valueConsumer = consumer
            binding.commentInput.setValueWithoutEventTrigger(consumer.get(), textListener)
        }
    }
}
