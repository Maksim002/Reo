package ru.ktsstudio.common.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SwipeDownBehavior<V : View>(
    context: Context,
    attributeSet: AttributeSet
) : BottomSheetBehavior<V>(context, attributeSet) {

    private var touchSlope = ViewConfiguration.get(context).scaledTouchSlop
    private var lastMotionY: Float = 0f
    private var isBeingDraggedDown = false

    override fun onInterceptTouchEvent(
        parent: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {

        val consumedY = event.y - lastMotionY

        val isOutsideChild = event.x > child.right ||
            event.x < child.left ||
            event.y < child.top ||
            event.y > child.bottom

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                lastMotionY = event.y
                isBeingDraggedDown = consumedY > touchSlope && isOutsideChild.not()
            }
            MotionEvent.ACTION_DOWN -> {
                lastMotionY = event.y
                isBeingDraggedDown = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isBeingDraggedDown = false
            }
        }

        return isBeingDraggedDown
    }
}