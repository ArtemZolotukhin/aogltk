package com.example.rz.gltest.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.Nullable;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;

public class TextureUtils {

    public static int[] loadTexture(Context context, int resourceId) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);

        return loadTexture(bitmap, true);

    }

    public static int[] loadTexture(@Nullable Bitmap bitmap, boolean recycleBitmap) {

        if (bitmap == null) {
            return new int[] {0};
        }

        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            if (recycleBitmap) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            return new int[] {0};
        }

        // настройка объекта текстуры
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        if (recycleBitmap) {
            bitmap.recycle();
        }

        // сброс target
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureIds;
    }
}

