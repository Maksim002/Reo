package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemDeleteEntityBinding
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setupInCard

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
class DeleteEntityItemAdapterDelegate<EntityQualifier>(
    private val onDelete: (id: String, qualifier: EntityQualifier) -> Unit
) : AbsListItemAdapterDelegate<
    DeleteEntityItem<EntityQualifier>,
    Any,
    DeleteEntityItemAdapterDelegate.Holder<EntityQualifier>>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder<EntityQualifier> {
        return Holder(
            binding = parent.inflate(ItemDeleteEntityBinding::inflate),
            onDelete = onDelete
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is DeleteEntityItem<*>
    }

    override fun onBindViewHolder(
        item: DeleteEntityItem<EntityQualifier>,
        holder: Holder<EntityQualifier>,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder<EntityQualifier>(
        private val binding: ItemDeleteEntityBinding,
        onDelete: (id: String, qualifier: EntityQualifier) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: DeleteEntityItem<EntityQualifier>? = null

        init {
            binding.deleteButton.setOnClickListener {
                currentItem?.let { onDelete(it.id, it.qualifier) }
            }
        }

        fun bind(item: DeleteEntityItem<EntityQualifier>) {
            binding.container.setupInCard(item.inCard)
            binding.root.isActivated = true
            currentItem = item
        }
    }
}
