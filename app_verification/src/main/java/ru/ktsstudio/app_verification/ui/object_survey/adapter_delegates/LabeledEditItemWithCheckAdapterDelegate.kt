package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemLabeledEditWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
class LabeledEditItemWithCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<LabeledEditItemWithCheck, Any, LabeledEditItemWithCheckAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLabeledEditWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledEditItemWithCheck
    }

    override fun onBindViewHolder(
        item: LabeledEditItemWithCheck,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as LabeledEditItemWithCheck)
                .checkableValueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemLabeledEditWithCheckBinding,
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
            binding.inputEditText.addTextChangedListener(textListener)
            checkBox.setOnCheckedChangeListener(checkBoxCheckChangeListener)
        }

        fun bind(item: LabeledEditItemWithCheck) = with(binding) {
            labelTextView.text = item.label
            inputEditText.hint = item.editHint
            inputEditText.isEnabled = item.enabled
            inputEditText.setValueWithoutEventTrigger(item.checkableValueConsumer.get(), textListener)
            inputEditText.updateInputTypeFormat(item.inputFormat)
            updateInput(item.checkableValueConsumer)
        }

        fun updateInput(consumer: CheckableValueConsumer<String?, *>) {
            checkableValueConsumer = consumer
            checkBox.setCheckedWithoutEventTrigger(consumer.isChecked, checkBoxCheckChangeListener)
        }
    }
}
