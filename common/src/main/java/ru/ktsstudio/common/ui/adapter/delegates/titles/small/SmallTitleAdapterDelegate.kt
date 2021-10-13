package ru.ktsstudio.common.ui.adapter.delegates.titles.small

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_small_title.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class SmallTitleAdapterDelegate : AbsListItemAdapterDelegate<
    SmallTitleItem,
    Any,
    SmallTitleAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_small_title)
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is SmallTitleItem
    }

    override fun onBindViewHolder(
        item: SmallTitleItem,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: SmallTitleItem) {
            titleTextView.text = item.text
        }
    }
}
