package com.example.rz.gltest.base

import android.opengl.GLSurfaceView

interface ExtendedRenderer : GLSurfaceView.Renderer {


    fun onPause() {}

    fun onResume() {}

    fun setOnRedrawListener(redrawListener: OnNeedRedrawListener)

}