package com.example.rz.gltest.base

import android.content.Context
import android.graphics.BitmapFactory
import com.example.rz.gltest.R

class TestFakeView(val context: Context) : View() {

    private var texture: GlTexture

    init {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(
                context.resources, R.drawable.zhban, options);
        texture = GlTexture(bitmap, true)

    }


    private var isDestroyed = false

    override fun getDrawObject(): DrawObject {
        return DrawObject().apply {
            glTexture = texture
            addTranslate(0.3f, 0.3f, 0f)
        }
    }

    override fun destroy() {
        if (!texture.isDestroyed()) {
            texture.destroy()
        }
    }

    override fun isDestroyed(): Boolean = isDestroyed

}