package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerEditBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.setupInCard
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

class InnerEditItemAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<InnerEditItem, Any, InnerEditItemAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerEditBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerEditItem
    }

    override fun onBindViewHolder(
        item: InnerEditItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as InnerEditItem)
                .valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemInnerEditBinding,
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

        fun bind(item: InnerEditItem) = with(binding) {
            root.isActivated = true
            container.setupInCard(item.inCard)
            inputEditText.hint = item.editHint
            inputEditText.updateInputTypeFormat(item.inputFormat)
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: ValueConsumer<String?, *>) {
            valueConsumer = consumer
            binding.inputEditText.setValueWithoutEventTrigger(consumer.get(), textListener)
        }
    }
}
