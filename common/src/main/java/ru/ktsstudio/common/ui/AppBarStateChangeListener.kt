package ru.ktsstudio.common.ui

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    private var currentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, vOffset: Int) {
        when {
            vOffset == 0 && currentState != State.EXPANDED -> {
                onStateChanged(State.EXPANDED)
                currentState = State.EXPANDED
            }
            abs(vOffset) >= appBarLayout.totalScrollRange && currentState != State.COLLAPSED -> {
                onStateChanged(State.COLLAPSED)
                currentState = State.COLLAPSED
            }
            currentState != State.IDLE -> {
                onStateChanged(State.IDLE)
                currentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(state: State?)

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }
}
