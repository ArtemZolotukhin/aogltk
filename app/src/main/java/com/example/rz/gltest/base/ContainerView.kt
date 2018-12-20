package com.example.rz.gltest.base

class ContainerView: View() {


    private var isDestroyed = false

    private val children: MutableList<View> = ArrayList()

    override fun getDrawObject(): DrawObject {


        val drawObject = DrawObject()
        drawObject.addTranslate(x, y ,z)

        for (child in children) {
            drawObject.addDrawObject(
                child.getDrawObject().apply {
                    addTranslate(x, y, z)
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
}