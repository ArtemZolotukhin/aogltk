package com.example.rz.gltest.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.DrawableRes

class BitmapUtils {
    companion object {

        fun fromResourses(@DrawableRes resId: Int, context: Context): Bitmap {

            val options = BitmapFactory.Options()
            options.inScaled = false
            return BitmapFactory.decodeResource(
                context.resources, resId, options
            );
        }
    }
}
