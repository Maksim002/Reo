package ru.ktsstudio.feature_mno_list.ui.details.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTopCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.EmptyListAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.ListErrorAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.PageLoadingAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.utils.isClassEqualTo

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
class MnoDetailsAdapter(
    onRetry: () -> Unit
) : BaseListAdapter(MnoDetailsItemCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            LabeledValueAdapterDelegate(),
            LabeledCardValueAdapterDelegate(),
            CardBottomCornersAdapterDelegate(),
            CardTopCornersAdapterDelegate(),
            CardTitleItemAdapterDelegate()
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    class MnoDetailsItemCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (!oldItem.isClassEqualTo(newItem)) return false
            return when (oldItem) {
                is TitleItem -> oldItem.text == (newItem as TitleItem).text
                is LabeledValueItem -> oldItem.label == (newItem as LabeledValueItem).label
                is CardTitleItem -> oldItem.text == (newItem as CardTitleItem).text
                is ListStateItem -> true
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }

}