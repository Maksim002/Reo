package ru.ktsstudio.common.ui.view

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import ru.ktsstudio.utilities.extensions.orFalse

class MarginInternalItemDecoration(
    @Dimension(unit = Dimension.PX)
    private val spaceHeight: Int,
    private val isTrailing: Boolean = false,
    private val needSpace: ((holder: RecyclerView.ViewHolder) -> Boolean)? = null
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        val isFirst: Boolean = index == 0

        val holder = parent.findContainingViewHolder(view)

        val addSpace = isFirst.not() && holder?.let { needSpace?.invoke(holder) }.orFalse()
        val space = if (addSpace) spaceHeight else 0
        if (isTrailing) {
            outRect.bottom = space
        } else {
            outRect.top = space
        }
    }
}
