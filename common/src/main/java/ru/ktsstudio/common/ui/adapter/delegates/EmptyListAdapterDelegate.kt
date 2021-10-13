package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_list_empty.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 24/03/2020.
 */
class EmptyListAdapterDelegate :
    AbsListItemAdapterDelegate<ListStateItem.EmptyList, Any, EmptyListAdapterDelegate.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            parent.inflate(R.layout.view_list_empty)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ListStateItem.EmptyList
    }

    override fun onBindViewHolder(
        item: ListStateItem.EmptyList,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: ListStateItem.EmptyList) {
            title.setText(item.title)
            message.setText(item.message)
        }
    }
}
