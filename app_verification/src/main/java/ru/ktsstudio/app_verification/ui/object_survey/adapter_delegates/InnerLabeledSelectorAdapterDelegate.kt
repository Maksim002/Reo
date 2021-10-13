package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerLabeledSelectorBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.ui.base_spinner.dropdown.DropDownAdapter
import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setupInCard

/**
 * Created by Igor Park on 25/10/2020.
 */
class InnerLabeledSelectorAdapterDelegate<T>(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    InnerLabeledSelector<T>,
    Any,
    InnerLabeledSelectorAdapterDelegate.Holder<T>
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder<T> {
        return Holder(
            binding = parent.inflate(ItemInnerLabeledSelectorBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is InnerLabeledSelector<*>
    }

    override fun onBindViewHolder(
        item: InnerLabeledSelector<T>,
        holder: Holder<T>,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            holder.configSelector(payloads.first() as InnerLabeledSelector<T>)
        }
    }

    class Holder<T>(
        private val binding: ItemInnerLabeledSelectorBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var valueConsumer: ValueConsumer<T?, *>? = null
        private var dropdownAdapter = DropDownAdapter<T>(binding.root.context)

        init {
            binding.dropdownList.setAdapter(dropdownAdapter)
            binding.dropdownList.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    binding.dropdownList.dismissDropDown()
                    val item = parent.getItemAtPosition(position) as UiDropdownItem<T>
                    valueConsumer?.consume(item.id)
                        ?.let(onDataChanged)
                }
        }

        fun bind(item: InnerLabeledSelector<T>) {
            configSelector(item)
            itemView.isActivated = item.isNested
            binding.labelTextView.isVisible = item.label.isNotBlank()
            binding.labelTextView.text = item.label
            binding.dropdownList.hint = item.hint
            binding.container.setupInCard(item.inCard)
        }

        fun configSelector(item: InnerLabeledSelector<T>) {
            valueConsumer = item.valueConsumer
            dropdownAdapter.setItems(item.items)
            binding.dropdownList.setText(item.selectedTitle)
            binding.dropdownList.dismissDropDown()
        }
    }
}
