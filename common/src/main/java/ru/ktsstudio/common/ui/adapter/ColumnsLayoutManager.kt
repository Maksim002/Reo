package ru.ktsstudio.common.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import ru.ktsstudio.common.R

/**
 * Created by Igor Park on 2019-11-15.
 */
class ColumnsLayoutManager(
    context: Context,
    tabletSpan: Int,
    phoneSpan: Int,
    rowPredicate: ((position: Int) -> Boolean)? = null
) : GridLayoutManager(
    context,
    if (context.resources.getBoolean(R.bool.isTablet) || rowPredicate != null) tabletSpan else phoneSpan
) {

    init {
        if (rowPredicate != null) defineSpanSizeLookup(
            context = context,
            tabletSpan = tabletSpan,
            phoneSpan = phoneSpan,
            rowPredicate = rowPredicate
        )
    }

    private fun defineSpanSizeLookup(
        context: Context,
        rowPredicate: ((position: Int) -> Boolean),
        tabletSpan: Int,
        phoneSpan: Int
    ) {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isTablet = context.resources.getBoolean(R.bool.isTablet)
                val isRow = rowPredicate(position)

                return when {
                    isTablet && isRow.not() -> phoneSpan
                    else -> tabletSpan
                }
            }
        }
    }
}
