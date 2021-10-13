package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.10.2020.
 */
class CardBottomCornersAdapterDelegate :
    AbsListItemAdapterDelegate<CardCornersItem, Any, CardBottomCornersAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(R.layout.item_card_corners)
            .also { it.rotation = ROTATION_BOTTOM }
            .let { Holder(it) }
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is CardCornersItem && item.isTop.not()
    }

    override fun onBindViewHolder(
        item: CardCornersItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(containerView: View) : BaseCardHolder(containerView)

    companion object {
        private const val ROTATION_BOTTOM = 180f
    }
}