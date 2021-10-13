package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemLabeledMultilineWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

class LabeledMultilineItemWithCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    LabeledMultilineItemWithCheck,
    Any,
    LabeledMultilineItemWithCheckAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLabeledMultilineWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledMultilineItemWithCheck
    }

    override fun onBindViewHolder(
        item: LabeledMultilineItemWithCheck,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as LabeledMultilineItemWithCheck)
                .valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemLabeledMultilineWithCheckBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var checkableValueConsumer: CheckableValueConsumer<String?, *>? = null
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        private val textListener = AfterTextChangedWatcher { newText ->
            checkableValueConsumer?.consume(newText)
                ?.let(onDataChanged)
        }

        private val checkBoxCheckChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                checkableValueConsumer?.setChecked(isChecked)
                    ?.let(onDataChanged)
            }

        init {
            binding.input.addTextChangedListener(textListener)
            checkBox.setOnCheckedChangeListener(checkBoxCheckChangeListener)
        }

        fun bind(item: LabeledMultilineItemWithCheck) = with(binding) {
            label.text = item.label
            input.hint = item.editHint
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: CheckableValueConsumer<String?, *>) {
            checkableValueConsumer = consumer

            binding.input.setValueWithoutEventTrigger(consumer.get(), textListener)
            checkBox.setCheckedWithoutEventTrigger(consumer.isChecked, checkBoxCheckChangeListener)
        }
    }
}
