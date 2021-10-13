package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemInnerLabeledSurveyBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
class InnerLabeledSurveyWithCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    InnerLabeledSurveyWithCheck,
    Any,
    InnerLabeledSurveyWithCheckAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerLabeledSurveyBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerLabeledSurveyWithCheck
    }

    override fun onBindViewHolder(
        item: InnerLabeledSurveyWithCheck,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as InnerLabeledSurveyWithCheck)
                .checkableValueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemInnerLabeledSurveyBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var checkableValueConsumer: CheckableValueConsumer<Boolean, *>? = null
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        private val radioCheckedChangeListener =
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val value = when (checkedId) {
                    binding.yesButton.id -> true
                    binding.noButton.id -> false
                    else -> false
                }
                checkableValueConsumer?.consume(value)
                    ?.let(onDataChanged)
            }

        private val checkBoxCheckChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                checkableValueConsumer?.setChecked(isChecked)
                    ?.let(onDataChanged)
            }

        init {
            binding.radioGroup.setOnCheckedChangeListener(radioCheckedChangeListener)
            checkBox.setOnCheckedChangeListener(checkBoxCheckChangeListener)
        }

        fun bind(item: InnerLabeledSurveyWithCheck) = with(binding) {
            labelTextView.text = item.label
            root.setBackgroundColor(ContextCompat.getColor(itemView.context, item.backgroundColor))

            updateInput(item.checkableValueConsumer)
        }

        fun updateInput(consumer: CheckableValueConsumer<Boolean, *>) {
            checkableValueConsumer = consumer
            checkBox.setCheckedWithoutEventTrigger(consumer.isChecked, checkBoxCheckChangeListener)
            val checkedId = when (consumer.get()) {
                true -> binding.yesButton.id
                false -> binding.noButton.id
            }
            checkedId.let {
                binding.radioGroup.setValueWithoutEventTrigger(
                    checkedId,
                    radioCheckedChangeListener
                )
            }
        }
    }
}
