package com.example.rz.gltest.base

abstract class View: Destroyable {

    var x = 0f

    var y = 0f

    var z = 0f

    var width = 0f

    var height = 0f

    abstract fun getDrawObject(): DrawObject

    /**
     * @return is intercept
     */
    fun handleTouch(x: Float, y: Float): Boolean = false

    /**
     * @return is intercept
     */
    fun handleLongTap(x: Float, y: Float): Boolean = false



}