package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemAddMediaBinding
import ru.ktsstudio.app_verification.presentation.object_survey.common.SurveyMediaUi
import ru.ktsstudio.common.utils.inflate

internal class AddMediaDelegate(
    private val onClick: () -> Unit
) : AbsListItemAdapterDelegate<
    SurveyMediaUi.AddItem,
    Any,
    AddMediaDelegate.ViewHolder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            binding = parent.inflate(ItemAddMediaBinding::inflate)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is SurveyMediaUi.AddItem
    }

    override fun onBindViewHolder(
        listItem: SurveyMediaUi.AddItem,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(onClick)
    }

    class ViewHolder(
        val binding: ItemAddMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var onClick: (() -> Unit)? = null

        init {
            binding.root.setOnClickListener {
                onClick?.invoke()
            }
        }

        fun bind(onClick: () -> Unit) {
            this.onClick = onClick
        }
    }
}
