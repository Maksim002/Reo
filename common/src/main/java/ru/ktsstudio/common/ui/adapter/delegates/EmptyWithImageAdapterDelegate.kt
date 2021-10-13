package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

class EmptyWithImageAdapterDelegate :
    AbsListItemAdapterDelegate<ListStateItem.EmptyList, Any, EmptyWithImageAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.view_not_found))
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ListStateItem.EmptyList
    }

    override fun onBindViewHolder(
        item: ListStateItem.EmptyList,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
