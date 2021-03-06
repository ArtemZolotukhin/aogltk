package com.example.rz.gltest.base.view

import android.support.annotation.IntDef
import com.example.rz.gltest.base.Destroyable
import com.example.rz.gltest.base.DrawObject

abstract class View: Destroyable {

    companion object {
        const val TOUCH_DOWN = 1
        const val TOUCH_HOLD = 2
        const val TOUCH_UP = 4
        const val TOUCH_LONG_TAP = 8
        const val SCREEN_SIZE = -1f
    }

    var x = 0f

    var y = 0f

    var z = 0f

    var width = SCREEN_SIZE

    var height = SCREEN_SIZE

    var parent: ViewParent? = null

    abstract fun getDrawObject(): DrawObject

    /**
     * @return is intercept
     */
    open fun handleTouch(x: Float, y: Float, @TouchType touchType: Int): Boolean = false


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [TOUCH_DOWN, TOUCH_HOLD, TOUCH_LONG_TAP, TOUCH_UP])
    annotation class TouchType

    open fun invalidate() {
        parent?.onChildUpdate(this)
    }


}