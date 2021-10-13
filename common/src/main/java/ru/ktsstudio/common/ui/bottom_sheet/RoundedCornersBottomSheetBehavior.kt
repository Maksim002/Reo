package ru.ktsstudio.common.ui.bottom_sheet

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.updateBackground

class RoundedCornersBottomSheetBehavior<V : View>(
    private val view: V,
    private val doOnSlide: ((slideOffset: Float) -> Unit)? = null,
    private val doOnStateChanged: ((newState: Int) -> Unit)? = null,
    originalBehavior: BottomSheetBehavior<View>? = null,
    @ColorRes private val colorRes: Int = android.R.color.white,
    @DimenRes private val cornerRadius: Int = R.dimen.bottom_sheet_corner_radius
) {

    private val pxCornerRadius: Float = view.resources.getDimension(cornerRadius)

    var peekHeight: Int
        get() = actualBehavior.peekHeight
        set(value) {
            actualBehavior.peekHeight = value
        }

    var state: Int
        get() = actualBehavior.state
        set(value) {
            actualBehavior.state = value
        }

    private val actualBehavior = (originalBehavior ?: BottomSheetBehavior.from(view)).apply {
        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val offset = slideOffset.takeIf { it.isFinite() } ?: 0f
                updateCornerRadius(offset)
                doOnSlide?.invoke(offset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                doOnStateChanged?.invoke(newState)
            }
        })
    }

    init {
        view.background = GradientDrawable().apply {
            setColor(
                ContextCompat.getColor(view.context, colorRes)
            )
        }
    }

    fun onViewStateRestored() {
        view.updateBackground {
            (it as? GradientDrawable)?.updateBottomSheetCorners(
                bottomSheetState = state,
                collapsedCornersRadius = pxCornerRadius
            )
        }
    }

    private fun updateCornerRadius(slideOffset: Float) {
        val reversedSlideOffset = 1 - slideOffset
        val bottomSheetFullyExpandable = view.height == (view.parent.parent as View).height
        val cornerRadiusSquared = if (bottomSheetFullyExpandable) {
            reversedSlideOffset * pxCornerRadius
        } else {
            pxCornerRadius
        }

        view.updateBackground {
            (it as? GradientDrawable)?.setCornersRadius(
                topLeft = cornerRadiusSquared,
                topRight = cornerRadiusSquared
            )
        }
    }

    private fun GradientDrawable.updateBottomSheetCorners(
        bottomSheetState: Int,
        collapsedCornersRadius: Float
    ) {
        val cornerRadius = when (bottomSheetState) {
            BottomSheetBehavior.STATE_EXPANDED -> 0f
            BottomSheetBehavior.STATE_COLLAPSED -> collapsedCornersRadius
            else -> null
        }

        cornerRadius?.let { setCornersRadius(topLeft = it, topRight = it) }
    }

    private fun GradientDrawable.setCornersRadius(
        topLeft: Float = 0f,
        topRight: Float = 0f,
        bottomLeft: Float = 0f,
        bottomRight: Float = 0f
    ) {
        cornerRadii = arrayOf(
            topLeft,
            topLeft,
            topRight,
            topRight,
            bottomRight,
            bottomRight,
            bottomLeft,
            bottomLeft
        )
            .toFloatArray()
    }
}
