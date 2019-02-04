package com.example.rz.gltest

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.rz.gltest.base.BaseGlSurfaceView
import com.example.rz.gltest.base.utils.BitmapUtils
import com.example.rz.gltest.base.view.Button
import com.example.rz.gltest.base.view.ContainerView
import com.example.rz.gltest.base.view.TestFakeView
import com.example.rz.gltest.base.view.View

class TextureRotationActivity : AppCompatActivity() {

    private var glSurfaceView: BaseGlSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(SucklessSurfaceView(this))
        setContentView(R.layout.activity_main)
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val rootView = ContainerView()

        val testView = TestFakeView(this)
        testView.x = 0.2f
        testView.width = View.SCREEN_SIZE
        testView.height = View.SCREEN_SIZE

        val clickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                testView.x += 0.1f
                if (testView.x > 1 ) {
                    testView.x -= 1
                }
                testView.invalidate()
            }
        }
        rootView.addChild(testView)
        for (i in 0..5) {
            val button = Button(BitmapUtils.Companion.fromResourses(R.drawable.button, this), true)
            button.onClickListener = clickListener
            button.x = 0.2f
            button.y = 0.2f * i
            button.width = 0.2f
            button.height = 0.2f
            rootView.addChild(button)
        }


        glSurfaceView = findViewById(R.id.glSurfaceView)
        glSurfaceView?.apply {
            setEGLContextClientVersion(2)
            setRenderer(OpenGlRenderer(this@TextureRotationActivity, this).apply {
                setRootView(rootView)
            })
        }
    }

    private fun supportES2(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
