package ru.ktsstudio.common.ui.fragment

import androidx.fragment.app.Fragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.06.2020.
 */
internal class FragmentDestroyChecker(
    private val onFullDestroy: () -> Unit
) {

    private var stateSaved: Boolean = false

    fun onSaveInstanceState(fragment: Fragment) {
        stateSaved = fragment.view == null
    }

    fun onDestroyView(fragment: Fragment) {
        stateSaved = fragment.view != null && fragment.requireActivity().isChangingConfigurations
    }

    // Moxy MvpFragment
    fun onDestroy(fragment: Fragment) {
        // We leave the screen and respectively all fragments will be destroyed
        if (fragment.requireActivity().isFinishing) {
            onFullDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (stateSaved) {
            stateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false

        var parent = fragment.parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (fragment.isRemoving || anyParentIsRemoving) {
            onFullDestroy()
        }
    }
}
