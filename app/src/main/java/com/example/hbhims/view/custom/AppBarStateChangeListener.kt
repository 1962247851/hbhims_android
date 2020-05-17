package com.example.hbhims.view.custom

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState =
        State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        when {
            i == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(
                        appBarLayout,
                        State.EXPANDED, i
                    )
                }
                mCurrentState =
                    State.EXPANDED
            }
            abs(i) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(
                        appBarLayout,
                        State.COLLAPSED, i
                    )
                }
                mCurrentState =
                    State.COLLAPSED
            }
            else -> {
                onStateChanged(
                    appBarLayout,
                    State.IDLE, i
                )
                mCurrentState =
                    State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State, i: Int)

}