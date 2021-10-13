package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_comment.*
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.reo.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class CommentaryAdapterDelegate(
    private val onTextChanged: (String) -> Unit
) : AbsListItemAdapterDelegate<
    CommentaryItem,
    Any,
    CommentaryAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_comment),
            onTextChanged
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is CommentaryItem
    }

    override fun onBindViewHolder(
        item: CommentaryItem,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder(
        override val containerView: View,
        onTextChanged: (String) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private val textListener = AfterTextChangedWatcher(onTextChanged)
        init {
            commentInput.addTextChangedListener(textListener)
        }

        fun bind(item: CommentaryItem) {
            commentInput.setValueWithoutEventTrigger(item.text, textListener)
        }
    }
}
