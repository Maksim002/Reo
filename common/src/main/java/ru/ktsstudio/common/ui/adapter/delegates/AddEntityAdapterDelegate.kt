package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_add_button.*
import ru.ktsstudio.common.R
import ru.ktsstudio.utilities.extensions.inflate
import ru.ktsstudio.utilities.extensions.updatePadding

/**
 * Created by Igor Park on 31/10/2020.
 */

class AddEntityAdapterDelegate<EntityQualifier>(private val onAddEntityClick: (EntityQualifier) -> Unit) :
    AbsListItemAdapterDelegate<AddEntityItem<EntityQualifier>, Any, AddEntityAdapterDelegate.Holder<EntityQualifier>>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder<EntityQualifier> {
        return Holder(parent.inflate(R.layout.item_add_button), onAddEntityClick)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is AddEntityItem<*>
    }

    override fun onBindViewHolder(
        item: AddEntityItem<EntityQualifier>,
        holder: Holder<EntityQualifier>,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder<EntityQualifier>(
        override val containerView: View,
        onAddEntityClick: (EntityQualifier) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var qualifier: EntityQualifier? = null

        init {
            addButton.setOnClickListener {
                qualifier?.let(onAddEntityClick)
            }
        }

        fun bind(item: AddEntityItem<EntityQualifier>) {
            qualifier = item.qualifier
            addEntityText.text = item.text
            addEntityIcon.setImageResource(item.icon)
            setNestedState(item.nested)
        }

        private fun setNestedState(isNested: Boolean) {
            val horizontalPadding = if (isNested) {
                containerView.context.resources.getDimensionPixelSize(R.dimen.default_double_padding)
            } else {
                0
            }
            val verticalPadding = if (isNested) {
                containerView.context.resources.getDimensionPixelSize(R.dimen.default_padding)
            } else {
                0
            }

            containerView.isActivated = isNested
            containerView.updatePadding(
                left = horizontalPadding,
                right = horizontalPadding,
                top = verticalPadding,
                bottom = verticalPadding
            )
        }
    }
}