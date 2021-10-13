package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_card_empty_line.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate
import ru.ktsstudio.utilities.extensions.updatePadding

class CardEmptyLineAdapterDelegate :
    AbsListItemAdapterDelegate<CardEmptyLine, Any, CardEmptyLineAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_card_empty_line))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is CardEmptyLine
    }

    override fun onBindViewHolder(item: CardEmptyLine, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: CardEmptyLine) {
            containerView.isActivated = item.isNested
            containerView.updatePadding(left = item.horizontalPadding, right = item.horizontalPadding)
            line.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                item.height
            )
        }
    }
}