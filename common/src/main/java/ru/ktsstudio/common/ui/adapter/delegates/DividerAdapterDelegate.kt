package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
class DividerAdapterDelegate :
    AbsListItemAdapterDelegate<DividerItem, Any, DividerAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_card_divider))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is DividerItem && item.inCard.not()
    }

    override fun onBindViewHolder(
        item: DividerItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
    }

    class Holder(containerView: View) : RecyclerView.ViewHolder(containerView)
}