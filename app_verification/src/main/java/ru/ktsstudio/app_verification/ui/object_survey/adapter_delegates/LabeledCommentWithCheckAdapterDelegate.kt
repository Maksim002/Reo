package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemLabeledCommentWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher

/**
 * @author Maxim Ovchinnikov on 07.12.2020.
 */
class LabeledCommentWithCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<LabeledCommentWithCheck, Any, LabeledCommentWithCheckAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLabeledCommentWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledCommentWithCheck
    }

    override fun onBindViewHolder(
        item: LabeledCommentWithCheck,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as LabeledCommentWithCheck).checkableValueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemLabeledCommentWithCheckBinding,
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
            binding.commentInput.addTextChangedListener(textListener)
            checkBox.setOnCheckedChangeListener(checkBoxCheckChangeListener)
        }

        fun bind(item: LabeledCommentWithCheck) = with(binding) {
            commentTitle.text = item.label
            commentInput.hint = item.editHint
            binding.commentInput.setValueWithoutEventTrigger(item.checkableValueConsumer.get(), textListener)
            updateInput(item.checkableValueConsumer)
        }

        fun updateInput(consumer: CheckableValueConsumer<String?, *>) {
            checkableValueConsumer = consumer
            checkBox.setCheckedWithoutEventTrigger(consumer.isChecked, checkBoxCheckChangeListener)
        }
    }
}
