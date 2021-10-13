package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerDeletableLabeledEditBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

class LabeledEditDeletableItemAdapterDelegate<EntityQualifier>(
    private val onDelete: (id: String, qualifier: EntityQualifier) -> Unit,
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    LabeledEditDeletableItem<EntityQualifier>,
    Any,
    LabeledEditDeletableItemAdapterDelegate.Holder<EntityQualifier>>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder<EntityQualifier> {
        return Holder(
            binding = parent.inflate(ItemInnerDeletableLabeledEditBinding::inflate),
            onDelete = onDelete,
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledEditDeletableItem<*>
    }

    override fun onBindViewHolder(
        item: LabeledEditDeletableItem<EntityQualifier>,
        holder: Holder<EntityQualifier>,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            holder.updateInput(payloads.first() as LabeledEditDeletableItem<EntityQualifier>)
        }
    }

    class Holder<EntityQualifier>(
        private val binding: ItemInnerDeletableLabeledEditBinding,
        onDelete: (id: String, qualifier: EntityQualifier) -> Unit,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: LabeledEditDeletableItem<EntityQualifier>? = null
        private val consumer: ValueConsumer<String?, *>?
            get() = currentItem?.valueConsumer

        private val textListener = AfterTextChangedWatcher { newText ->
            consumer?.consume(newText)
                ?.let(onDataChanged)
        }

        init {
            binding.inputEditText.addTextChangedListener(textListener)
            binding.delete.setOnClickListener {
                currentItem?.let { onDelete(it.id, it.qualifier) }
            }
        }

        fun bind(item: LabeledEditDeletableItem<EntityQualifier>) = with(binding) {
            root.isActivated = true
            currentItem = item
            labelTextView.text = item.label
            inputEditText.hint = item.editHint
            inputEditText.updateInputTypeFormat(item.inputFormat)
            updateInput(item)
        }

        fun updateInput(item: LabeledEditDeletableItem<EntityQualifier>) {
            currentItem = item
            binding.inputEditText.setValueWithoutEventTrigger(consumer?.get(), textListener)
        }
    }
}
