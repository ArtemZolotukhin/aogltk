package com.example.rz.gltest.base.view

import com.example.rz.gltest.base.DrawObject

class ContainerView : ViewParent, View() {


    private var isDestroyed = false

    private val children: MutableList<View> = ArrayList()

    private var isNeedUpdate = true


    private var cachedDrawObject: DrawObject? = null

    override fun getDrawObject(): DrawObject {

        val drawObject = if (isNeedUpdate) {
            updateDrawObject()
        } else {
            cachedDrawObject ?: updateDrawObject()
        }
        cachedDrawObject = drawObject

        isNeedUpdate = false

        return drawObject
    }

    private fun updateDrawObject(): DrawObject {
        val drawObject = DrawObject()
        drawObject.addTranslate(x, y, z)

        for (child in children) {
            drawObject.addDrawObject(
                child.getDrawObject().apply {
                    addTranslate(x + child.x, y + child.y, z + child.z)
                }
            )
        }
        return drawObject
    }

    fun addChild(view: View) {
        view.parent = this
        children.add(view)
    }

    fun removeChild(view: View) {
        view.parent = null
        children.remove(view)
    }

    override fun destroy() {
        isDestroyed = true
    }

    override fun isDestroyed(): Boolean = isDestroyed

    override fun handleTouch(x: Float, y: Float, touchType: Int): Boolean {
        for (child in children) {
            if (child.handleTouch(x, y, touchType)) {
                return true
            }
        }
        return super.handleTouch(x, y, touchType)
    }

    override fun onChildUpdate(view: View) {
        invalidate()
    }

    override fun invalidate() {
        super.invalidate()
        isNeedUpdate = true
    }

}