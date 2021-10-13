package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_list_error.*
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.isNetworkUnavailableException
import ru.ktsstudio.utilities.extensions.inflate

typealias ErrorTitleWithMessage = Pair<String, String>

class ListErrorAdapterDelegate(private val onRetry: () -> Unit) :
    AbsListItemAdapterDelegate<ListStateItem.ErrorList, Any, ListErrorAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            onRetry = onRetry,
            containerView = parent.inflate(R.layout.view_list_error)
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean = item is ListStateItem.ErrorList

    override fun onBindViewHolder(
        item: ListStateItem.ErrorList,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        onRetry: () -> Unit,
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init {
            retryButton.setOnClickListener { onRetry.invoke() }
        }

        fun bind(item: ListStateItem.ErrorList) {
            val content = getErrorTitleWithMessage(item.error)
            title.text = content.first
            message.text = content.second
        }

        private fun getErrorTitleWithMessage(error: Throwable): ErrorTitleWithMessage {
            val isNetworkUnavailable = error.isNetworkUnavailableException()
            val resources = containerView.context.resources
            return if (isNetworkUnavailable) {
                resources.getString(R.string.error_title_network_unavailable) to
                    resources.getString(R.string.error_message_network_unavailable)
            } else {
                resources.getString(R.string.default_server_error_title) to
                    (error.localizedMessage?.let {
                        resources.getString(R.string.error_message_server_error, it)
                    } ?: resources.getString(R.string.error_page_message))
            }
        }
    }
}
