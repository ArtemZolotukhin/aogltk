package com.example.rz.gltest.base

class DrawObject {

    var width = 1f

    var height = 1f

    var x = 0f

    var y = 0f

    var z = 0f

    var glTexture: GlTexture? = null

    var translateX = 0f
        private set

    var translateY = 0f
        private set

    var translateZ = 0f
        private set

    private val drawObjects: MutableList<DrawObject> = ArrayList()

    fun addTranslate(x: Float, y: Float, z: Float) {
        drawObjects.forEach {
            it.addTranslate(x, y, z)
        }
        translateX += x
        translateY += y
        translateZ += z
    }

    fun translate(x: Float, y: Float, z: Float) {
        val dx = x - translateX
        val dy = y - translateY
        val dz = z - translateZ
        drawObjects.forEach {
            it.addTranslate(dx, dy, dz)
        }
        translateX = x
        translateY = y
        translateZ = z
    }

    fun addDrawObject(drawObject: DrawObject) {
        drawObject.addTranslate(translateX, translateY, translateZ)
        //TODO Sort
        drawObjects.add(drawObject)
    }

    fun getAllDrawObjectsAndSelf(): List<DrawObject> {
        val allObjects = ArrayList<DrawObject>()
        allObjects.add(this)
        for (drawObject in drawObjects) {
            allObjects.addAll(drawObject.getAllDrawObjectsAndSelf())
        }
        return allObjects
    }


}