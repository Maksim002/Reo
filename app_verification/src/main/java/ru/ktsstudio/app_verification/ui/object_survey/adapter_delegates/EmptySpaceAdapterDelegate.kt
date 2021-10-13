package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemInnerEmptySpaceBinding
import ru.ktsstudio.common.utils.inflate

/**
 * @author Maxim Ovchinnikov on 10.12.2020.
 */
class EmptySpaceAdapterDelegate :
    AbsListItemAdapterDelegate<EmptySpace, Any, EmptySpaceAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerEmptySpaceBinding::inflate)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is EmptySpace
    }

    override fun onBindViewHolder(
        item: EmptySpace,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(private val binding: ItemInnerEmptySpaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EmptySpace) {
            binding.root.isActivated = item.isNested
        }
    }
}
