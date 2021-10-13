package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerCheckBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * Created by Igor Park on 09/02/2021.
 */
class InnerCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<InnerCheckItem, Any, InnerCheckAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerCheckItem
    }

    override fun onBindViewHolder(
        item: InnerCheckItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemInnerCheckBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var valueConsumer: ValueConsumer<Boolean?, *>? = null
        private val checkBoxCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            valueConsumer?.consume(isChecked)
                ?.let(onDataChanged)
        }

        init {
            binding.check.setOnCheckedChangeListener(checkBoxCheckChangeListener)
        }

        fun bind(item: InnerCheckItem) = with(binding) {
            valueConsumer = item.valueConsumer
            root.isActivated = true
            check.text = item.title
            check.setCheckedWithoutEventTrigger(
                item.valueConsumer.get().orFalse(),
                checkBoxCheckChangeListener
            )
        }
    }
}
