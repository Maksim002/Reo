package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemDeletableReferenceBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate

class DeletableReferenceAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    DeletableReferenceItem,
    Any,
    DeletableReferenceAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemDeletableReferenceBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is DeletableReferenceItem
    }

    override fun onBindViewHolder(item: DeletableReferenceItem, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemDeletableReferenceBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItemId: String? = null
        private var valueConsumer: ValueConsumer<String?, *>? = null

        init {
            binding.delete.setOnClickListener {
                currentItemId?.let {
                    valueConsumer?.consume(it)
                        ?.let(onDataChanged)
                }
            }
        }

        fun bind(item: DeletableReferenceItem) {
            binding.labelTextView.text = item.title
            currentItemId = item.id
            valueConsumer = item.valueConsumer
        }
    }
}
