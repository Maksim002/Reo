package ru.ktsstudio.feature_mno_list.ui.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_mno.*
import ru.ktsstudio.common.utils.resourcesLocale
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.presentation.list.MnoListItemUi
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal class MnoItemDelegate(
    private val onClick: (mnoId: String) -> Unit
) : AbsListItemAdapterDelegate<MnoListItemUi, Any, MnoItemDelegate.ViewHolder>() {

    private val containersViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            containerView = parent.inflate(R.layout.item_mno),
            containersViewPool = containersViewPool,
            onClick = onClick
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is MnoListItemUi
    }

    override fun onBindViewHolder(listItem: MnoListItemUi, viewHolder: ViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(listItem)
    }

    class ViewHolder(
        override val containerView: View,
        containersViewPool: RecyclerView.RecycledViewPool,
        onClick: (mnoId: String) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val containersAdapter = ContainerItemAdapter()
        private var currentItem: MnoListItemUi? = null

        init {
            containerView.setOnClickListener {
                currentItem?.let { onClick(it.id) }
            }
            containers.adapter = containersAdapter
            containers.setRecycledViewPool(containersViewPool)
        }

        fun bind(listItem: MnoListItemUi) {
            currentItem = listItem
            val measureCountText = containerView.context.resourcesLocale()
                .getQuantityString(
                    R.plurals.mno_item_measure_count,
                    listItem.measureCount,
                    listItem.measureCount
                )
            source.text = listItem.sourceName
            measureCount.isVisible = listItem.measureCount > 0
            measureCount.text = measureCountText
            measureCount.requestLayout()
            category.text = listItem.categoryName
            address.text = listItem.address
            containersTitle.isVisible = listItem.containers.isNotEmpty()
            containersAdapter.submitList(listItem.containers)
        }
    }
}