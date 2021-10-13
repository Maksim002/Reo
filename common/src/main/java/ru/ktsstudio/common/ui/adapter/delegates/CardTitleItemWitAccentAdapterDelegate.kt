package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_card_title.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

class CardTitleItemWitAccentAdapterDelegate :
    AbsListItemAdapterDelegate<CardTitleItem, Any, CardTitleItemWitAccentAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_card_title_with_accent))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is CardTitleItem && item.withAccent
    }

    override fun onBindViewHolder(item: CardTitleItem, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: CardTitleItem) {
            titleTextView.text = item.text
        }
    }
}