package ru.ktsstudio.common.utils.keyboard

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.ktsstudio.utilities.KeyboardUtils

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2020.
 */

fun Fragment.setKeyboardListener(callback: (Boolean) -> Unit) {
    val listener = KeyboardUtils.SoftKeyboardToggleListener { callback(it) }
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            KeyboardUtils.addKeyboardToggleListener(requireActivity(), listener)
        }

        override fun onPause(owner: LifecycleOwner) {
            KeyboardUtils.removeKeyboardToggleListener(listener)
        }
    })
}