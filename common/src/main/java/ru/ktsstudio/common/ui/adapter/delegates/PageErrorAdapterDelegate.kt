package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_error_page.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate

class PageErrorAdapterDelegate(private val onRetryClick: () -> Unit) :
    AbsListItemAdapterDelegate<ListStateItem.ErrorPage, Any, PageErrorAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            onRetryClick = onRetryClick,
            containerView = parent.inflate(R.layout.item_error_page)
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean = item is ListStateItem.ErrorPage

    override fun onBindViewHolder(
        item: ListStateItem.ErrorPage,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
    }

    class Holder(
        onRetryClick: () -> Unit,
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init {
            retryButton.setOnClickListener { onRetryClick.invoke() }
        }
    }
}
