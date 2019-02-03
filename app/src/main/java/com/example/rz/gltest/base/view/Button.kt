package com.example.rz.gltest.base.view

import android.graphics.Bitmap
import android.util.Log
import com.example.rz.gltest.base.DrawObject
import com.example.rz.gltest.base.GlTexture

class Button(val bitmap: Bitmap, val takeCareBitmap: Boolean): View() {


    var onClickListener: OnClickListener? = null

    var glTexture: GlTexture = GlTexture(bitmap, false)

    private var isDestroyed = false


    override fun getDrawObject(): DrawObject {
        return DrawObject().apply {
            glTexture = this@Button.glTexture
            width = this@Button.width
            height = this@Button.height
        }
    }

    override fun destroy() {
        isDestroyed = true
        if (takeCareBitmap) {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
        if (!glTexture.isDestroyed()) {
            glTexture.destroy()
        }
    }

    override fun isDestroyed(): Boolean = isDestroyed

    override fun handleTouch(x: Float, y: Float, touchType: Int): Boolean {
        Log.d("Button", "touch_type = $touchType, x = $x, y = $y")
        if (touchType == TOUCH_UP) {
            if ((x > this.x) and (x < this.x + width) and (y > this.y) and (y < this.y + height)) {
                onClickListener?.onClick(this)
                return true
            }
        }
        return false
    }

    interface OnClickListener {
        fun onClick(view: View)
    }

}