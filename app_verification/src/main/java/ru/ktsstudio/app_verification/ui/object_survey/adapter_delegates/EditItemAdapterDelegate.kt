package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemLabeledEditWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

/**
 * Created by Igor Park on 04/12/2020.
 */
class EditItemAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<EditItem, Any, EditItemAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLabeledEditWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is EditItem
    }

    override fun onBindViewHolder(
        item: EditItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as EditItem)
                .valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemLabeledEditWithCheckBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var valueConsumer: ValueConsumer<String?, *>? = null
        private val textListener = AfterTextChangedWatcher { newText ->
            valueConsumer?.consume(newText)
                ?.let(onDataChanged)
        }

        init {
            binding.checkBox.isInvisible = true
            binding.inputEditText.addTextChangedListener(textListener)
        }

        fun bind(item: EditItem) = with(binding) {
            labelTextView.isVisible = item.label.isNullOrBlank().not()
            labelTextView.text = item.label
            inputEditText.hint = item.editHint
            inputEditText.isActivated = item.isEditable.not()
            inputEditText.isEnabled = item.isEditable
            inputEditText.updateInputTypeFormat(item.inputFormat)
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: ValueConsumer<String?, *>) {
            valueConsumer = consumer
            binding.inputEditText.setValueWithoutEventTrigger(consumer.get(), textListener)
        }
    }
}
