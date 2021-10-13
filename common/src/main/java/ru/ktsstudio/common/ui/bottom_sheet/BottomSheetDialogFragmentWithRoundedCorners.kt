package ru.ktsstudio.common.ui.bottom_sheet

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.extensions.LayoutContainer
import ru.ktsstudio.common.R
import ru.ktsstudio.common.ui.fragment.FragmentDestroyChecker
import ru.ktsstudio.common.utils.view.autoCleared
/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.06.2020.
 */

abstract class BottomSheetDialogFragmentWithRoundedCorners<V : View>(
    @LayoutRes private val resId: Int
) : BottomSheetDialogFragment(), LayoutContainer {

    private val destroyChecker = FragmentDestroyChecker(::onFullDestroy)
    protected var bottomSheetBehavior: RoundedCornersBottomSheetBehavior<V> by autoCleared()
        private set
    private var contentView: V by autoCleared()

    abstract fun initView(contentView: View)
    protected var fitsSystemWindows: Boolean = false

    override val containerView: View?
        get() = contentView

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        restoreBottomSheetState(savedInstanceState)
        super.onViewStateRestored(savedInstanceState)
    }

    private fun restoreBottomSheetState(savedInstanceState: Bundle?) {
        val savedState = savedInstanceState?.getInt(KEY_BOTTOM_SHEET_STATE).takeIf { it != 0 }
        if (savedState != null) {
            bottomSheetBehavior.state = savedState
        }
        bottomSheetBehavior.onViewStateRestored()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_BOTTOM_SHEET_STATE, bottomSheetBehavior.state)
        destroyChecker.onSaveInstanceState(this)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyChecker.onDestroyView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyChecker.onDestroy(this)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        (contentView.parent.parent.parent as View).fitsSystemWindows = fitsSystemWindows
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.RoundedBottomDialogStyle)

        contentView = DataBindingUtil.inflate<ViewDataBinding>(
            requireActivity().layoutInflater,
            resId,
            null,
            false
        )
            .root as V

        dialog.setContentView(contentView)
        setBottomSheetBehavior(contentView)
        restoreBottomSheetState(savedInstanceState)
        initView(contentView)
        return dialog
    }

    protected open fun onFullDestroy() {}

    private fun setBottomSheetBehavior(rootView: V) {
        val layoutParams = (rootView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior as BottomSheetBehavior
        bottomSheetBehavior = RoundedCornersBottomSheetBehavior(
            view = rootView,
            originalBehavior = behavior,
            doOnStateChanged = {
                if (it == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            })
    }

    companion object {
        private const val KEY_BOTTOM_SHEET_STATE = "KEY_BOTTOM_SHEET_STATE"
    }
}
