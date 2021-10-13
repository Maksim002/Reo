package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerLabeledEditBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.setupInCard
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
class InnerLabeledEditItemAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<InnerLabeledEditItem, Any, InnerLabeledEditItemAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerLabeledEditBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerLabeledEditItem
    }

    override fun onBindViewHolder(
        item: InnerLabeledEditItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as InnerLabeledEditItem)
                .valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemInnerLabeledEditBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var valueConsumer: ValueConsumer<String?, *>? = null
        private val textListener = AfterTextChangedWatcher { newText ->
            valueConsumer?.consume(newText)
                ?.let(onDataChanged)
        }

        init {
            binding.inputEditText.addTextChangedListener(textListener)
        }

        fun bind(item: InnerLabeledEditItem) = with(binding) {
            root.isActivated = true
            container.setupInCard(item.inCard)
            labelTextView.text = item.label
            inputEditText.hint = item.editHint
            inputEditText.isEnabled = item.enabled
            inputEditText.updateInputTypeFormat(item.inputFormat)
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: ValueConsumer<String?, *>) {
            valueConsumer = consumer
            binding.inputEditText.setValueWithoutEventTrigger(consumer.get(), textListener)
        }
    }
}
