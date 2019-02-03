package com.example.rz.gltest.base.view

import android.content.Context
import android.graphics.BitmapFactory
import com.example.rz.gltest.R
import com.example.rz.gltest.base.DrawObject
import com.example.rz.gltest.base.GlTexture

class TestFakeView(val context: Context) : View() {

    private var texture: GlTexture

    init {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(
                context.resources, R.drawable.abb0b634, options)
        texture = GlTexture(bitmap, true)

    }


    private var isDestroyed = false

    override fun getDrawObject(): DrawObject {
        return DrawObject().apply {
            glTexture = texture
            width = this@TestFakeView.width
            height = this@TestFakeView.height
            z = -0.1f
        }
    }

    override fun destroy() {
        if (!texture.isDestroyed()) {
            texture.destroy()
        }
    }

    override fun isDestroyed(): Boolean = isDestroyed

}