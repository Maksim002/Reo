package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerLargeTitleBinding
import ru.ktsstudio.common.utils.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.10.2020.
 */
class InnerLargeTitleAdapterDelegate : AbsListItemAdapterDelegate<
    LargeTitleItem,
    Any,
    InnerLargeTitleAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerLargeTitleBinding::inflate)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LargeTitleItem && item.isNested
    }

    override fun onBindViewHolder(
        item: LargeTitleItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemInnerLargeTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LargeTitleItem) = with(binding) {
            titleTextView.text = item.text
        }
    }
}
