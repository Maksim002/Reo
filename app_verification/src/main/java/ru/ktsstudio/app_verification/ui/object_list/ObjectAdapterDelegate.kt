package ru.ktsstudio.app_verification.ui.object_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemObjectBinding
import ru.ktsstudio.app_verification.presentation.object_list.ObjectListItemUi
import ru.ktsstudio.common.utils.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.11.2020.
 */
class ObjectAdapterDelegate(
    private val onClick: (objectId: String) -> Unit
) : AbsListItemAdapterDelegate<ObjectListItemUi, Any, ObjectAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemObjectBinding::inflate),
            onClick = onClick
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ObjectListItemUi
    }

    override fun onBindViewHolder(
        item: ObjectListItemUi,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemObjectBinding,
        onClick: (objectId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var objectId: String? = null

        init {
            itemView.setOnClickListener {
                objectId?.let { onClick(it) }
            }
        }

        fun bind(item: ObjectListItemUi) = with(binding) {
            objectId = item.id
            nameTextView.text = item.name
            addressTextView.text = item.address
            dateTextView.text = item.date
            statusTextView.text = item.status
            typeTextView.text = item.type
        }
    }
}
