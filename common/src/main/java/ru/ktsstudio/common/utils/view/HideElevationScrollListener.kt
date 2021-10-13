package ru.ktsstudio.common.utils.view

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
class HideElevationScrollListener(
    private val view: View,
    private val targetElevation: Int
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val directionDown = 1
        val viewElevation = if (recyclerView.canScrollVertically(directionDown)) targetElevation else 0
        ViewCompat.setElevation(view, viewElevation.toFloat())
    }

}