package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_labeled_value.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.10.2020.
 */
class LabeledCardValueAdapterDelegate :
    AbsListItemAdapterDelegate<LabeledValueItem, Any, LabeledCardValueAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(R.layout.item_labeled_value).let { Holder(it) }
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is LabeledValueItem && item.inCard
    }

    override fun onBindViewHolder(
        item: LabeledValueItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            setCardFrames()
        }

        fun bind(item: LabeledValueItem) {
            labelTextView.text = item.label
            valueTextView.text = item.value
        }

        private fun setCardFrames() {
            containerView.setBackgroundResource(R.drawable.bg_card_sides)
            val horizontalPadding =
                containerView.context.resources.getDimensionPixelSize(R.dimen.default_double_padding)
            val verticalPadding =
                containerView.context.resources.getDimensionPixelSize(R.dimen.default_cell_size)
            containerView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        }
    }
}