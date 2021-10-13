package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_double_labeled_value.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
class DoubleLabeledValueAdapterDelegate : AbsListItemAdapterDelegate<DoubleLabeledValueItem, Any, DoubleLabeledValueAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_double_labeled_value))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is DoubleLabeledValueItem && item.inCard.not()
    }

    override fun onBindViewHolder(
        item: DoubleLabeledValueItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: DoubleLabeledValueItem) {
            leftLabelTextView.text = item.leftLabel
            leftValueTextView.text = item.leftValue

            rightLabelTextView.text = item.rightLabel
            rightValueTextView.text = item.rightValue
        }
    }
}