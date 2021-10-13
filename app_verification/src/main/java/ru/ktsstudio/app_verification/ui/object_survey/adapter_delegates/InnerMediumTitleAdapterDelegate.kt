package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerMediumTitleBinding
import ru.ktsstudio.app_verification.ui.common.setParagraphSmallBold
import ru.ktsstudio.common.utils.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class InnerMediumTitleAdapterDelegate : AbsListItemAdapterDelegate<
    InnerMediumTitle,
    Any,
    InnerMediumTitleAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(ItemInnerMediumTitleBinding::inflate)
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is InnerMediumTitle
    }

    override fun onBindViewHolder(
        item: InnerMediumTitle,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder(
        val binding: ItemInnerMediumTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InnerMediumTitle) {
            binding.root.isActivated = item.filled
            binding.titleTextView.text = item.text
            binding.titleTextView.setParagraphSmallBold(item.withAccent)
        }
    }
}
