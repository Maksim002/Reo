package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemLabeledSelectorWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.ui.base_spinner.dropdown.DropDownAdapter
import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger

/**
 * Created by Igor Park on 25/10/2020.
 */
class LabeledSelectorWithCheckAdapterDelegate<T>(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    LabeledSelectorWithCheck<T>,
    Any,
    LabeledSelectorWithCheckAdapterDelegate.Holder<T>
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder<T> {
        return Holder(
            binding = parent.inflate(ItemLabeledSelectorWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is LabeledSelectorWithCheck<*>
    }

    override fun onBindViewHolder(
        item: LabeledSelectorWithCheck<T>,
        holder: Holder<T>,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            holder.configSelector(payloads.first() as LabeledSelectorWithCheck<T>)
        }
    }

    class Holder<T>(
        private val binding: ItemLabeledSelectorWithCheckBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var valueConsumer: CheckableValueConsumer<T?, *>? = null
        private var dropdownAdapter = DropDownAdapter<T>(binding.root.context)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            valueConsumer?.setChecked(isChecked)
                ?.let(onDataChanged)
        }

        init {
            binding.dropdownList.setAdapter(dropdownAdapter)
            binding.dropdownList.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    binding.dropdownList.dismissDropDown()
                    val item = parent.getItemAtPosition(position) as UiDropdownItem<T>
                    valueConsumer?.consume(item.id)
                        ?.let(onDataChanged)
                }
            checkBox.setOnCheckedChangeListener(checkedChangeListener)
        }

        fun bind(item: LabeledSelectorWithCheck<T>) {
            configSelector(item)
            binding.dropdownList.hint = item.hint
            binding.labelTextView.text = item.label
        }

        fun configSelector(item: LabeledSelectorWithCheck<T>) {
            valueConsumer = item.valueConsumer
            dropdownAdapter.setItems(item.items)
            binding.dropdownList.setText(item.selectedTitle)
            binding.dropdownList.dismissDropDown()
            checkBox.setCheckedWithoutEventTrigger(item.valueConsumer.isChecked, checkedChangeListener)
        }
    }
}
