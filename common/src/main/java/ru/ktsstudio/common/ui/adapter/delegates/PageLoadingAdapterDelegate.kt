package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_loading_page.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 24/03/2020.
 */
class PageLoadingAdapterDelegate :
    AbsListItemAdapterDelegate<ListStateItem.LoadingPage, Any, PageLoadingAdapterDelegate.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            parent.inflate(R.layout.item_loading_page)
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ListStateItem.LoadingPage
    }

    override fun onBindViewHolder(
        item: ListStateItem.LoadingPage,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: ListStateItem.LoadingPage) {
            loadRoot.layoutParams = if (item.isListLoading) {
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
            } else {
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
        }
    }
}
