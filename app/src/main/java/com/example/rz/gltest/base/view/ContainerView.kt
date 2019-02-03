package com.example.rz.gltest.base.view

import com.example.rz.gltest.base.DrawObject

class ContainerView : View() {


    private var isDestroyed = false

    private val children: MutableList<View> = ArrayList()

    override fun getDrawObject(): DrawObject {


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
        children.add(view)
    }

    fun removeChild(view: View) {
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


}