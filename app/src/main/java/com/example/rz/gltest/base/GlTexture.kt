package com.example.rz.gltest.base

import android.graphics.Bitmap
import android.opengl.GLES20.glDeleteTextures
import com.example.rz.gltest.base.utils.TextureUtils

class GlTexture(val bitmap: Bitmap, var takeCareBitmap: Boolean): Destroyable {

    private var isDestroyed: Boolean = false

    private var textureRef: IntArray? = null

    fun getTextureRef(): IntArray {
        return textureRef ?: TextureUtils.loadTexture(bitmap, takeCareBitmap).apply { textureRef = this }
    }

    override fun destroy() {
        isDestroyed = true
        glDeleteTextures(1, textureRef, 0)
        if (takeCareBitmap) {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
    }

    override fun isDestroyed(): Boolean = isDestroyed

}