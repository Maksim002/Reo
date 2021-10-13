package ru.ktsstudio.common.ui.adapter.delegates

import android.view.View
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.ktsstudio.common.R

abstract class BaseCardHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: CardCornersItem) {
        containerView.isActivated = item.isNested
        val horizontalPadding = if (item.isNested) {
            containerView.context.resources.getDimensionPixelSize(R.dimen.default_side_padding)
        } else {
            0
        }
        containerView.updatePadding(left = horizontalPadding, right = horizontalPadding)
    }
}