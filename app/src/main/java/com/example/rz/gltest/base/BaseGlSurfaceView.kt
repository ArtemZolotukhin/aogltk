package com.example.rz.gltest.base

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class BaseGlSurfaceView : GLSurfaceView, OnNeedRedrawListener {

    private var extendedRenderer: ExtendedRenderer? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun setRenderer(renderer: Renderer?) {
        super.setRenderer(renderer)
        if (renderer != null) {
            if (renderer is ExtendedRenderer) {
                extendedRenderer = renderer
                renderer.setOnRedrawListener(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        extendedRenderer?.onPause()
    }

    override fun onResume() {
        super.onResume()
        renderMode = RENDERMODE_WHEN_DIRTY
        extendedRenderer?.onResume()
    }

    override fun onNeedRedraw(isNeedRedraw: Boolean) {
        requestRender()
    }

}