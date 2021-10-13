package ru.ktsstudio.common.utils.view

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

class ElevationOnScrollListener(
    private val bottomBlock: View,
    private val targetElevation: Int
) : NestedScrollView.OnScrollChangeListener {
    override fun onScrollChange(
        scrollView: NestedScrollView,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val directionDown = 1
        val elevation = if (scrollView.canScrollVertically(directionDown)) targetElevation.toFloat() else 0f
        ViewCompat.setElevation(bottomBlock, elevation)
    }
}
