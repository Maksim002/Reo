package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemLargeTitleBinding
import ru.ktsstudio.common.utils.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.10.2020.
 */
class LargeTitleItemAdapterDelegate :
    AbsListItemAdapterDelegate<LargeTitleItem, Any, LargeTitleItemAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemLargeTitleBinding::inflate)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LargeTitleItem
    }

    override fun onBindViewHolder(item: LargeTitleItem, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemLargeTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LargeTitleItem) = with(binding) {
            titleTextView.text = item.text
        }
    }
}
