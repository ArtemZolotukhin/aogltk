package com.example.rz.gltest;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import com.example.rz.gltest.base.DrawObject;
import com.example.rz.gltest.base.GlTexture;
import com.example.rz.gltest.base.View;
import com.example.rz.gltest.base.utils.ShaderUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;


public class OpenGlRenderer implements Renderer {

    private static final String LOG_TAG = "OGLR";

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private Context context;

    private FloatBuffer vertexData;

    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];

    private View rootView;

    public OpenGlRenderer(Context context) {
        this.context = context;
    }

    public void setRootView(View view) {
        rootView = view;
    }



    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        createAndUseProgram();
        getLocations();
        glActiveTexture(GL_TEXTURE0);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createViewMatrix();
        createProjectionMatrix(width, height);
        prepareData(width, height);
        bindData();
        resetModelMatrix();
        bindMatrix();
    }

    /**
     *
     * @param w - width in pixels
     * @param h - height in pixels
     */
    private void prepareData(float w, float h) {

        /* TODO handle when w bigger than h */
        //ratio
        float r = h / w;

        float[] vertices = {
                0f, 1 * r, 1, 0, 0,
                0f, 0f, 1, 0, 1f,
                1, 1 * r, 1, 1f, 0,
                1, 0f, 1, 1, 1f,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }


    private void createAndUseProgram() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    private void getLocations() {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void bindData() {
        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture[0]);

        // юнит текстурыglUniform1i
        glUniform1i(uTextureUnitLocation, 0);
    }


    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = 0;
        float right = 1;
        float bottom = 0;
        float top = 1;
        float near = 0;
        float far = 1;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
//            bottom *= ratio;
            top *= ratio;
        }
        Log.d(LOG_TAG, "top = " + top + " bottom + " + bottom);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 1;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }


    @Override
    public void onDrawFrame(GL10 arg0) {

        long lastTime = System.currentTimeMillis();

        if (rootView != null) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            GlTexture iTexture;

            float ix = 0f;
            float iy = 0f;
            float iz = 0f;

            for (DrawObject drawObject: rootView.getDrawObject().getAllDrawObjectsAndSelf()) {
                iTexture = drawObject.getGlTexture();
                if (!(iTexture == null || iTexture.isDestroyed())) {
                    resetModelMatrix();
                    ix = drawObject.getX() + drawObject.getTranslateX();
                    iy = drawObject.getY() + drawObject.getTranslateY();
                    iz = drawObject.getZ() + drawObject.getTranslateZ();
                    scaleModelMatrix(drawObject.getWidth(), drawObject.getHeight());
                    translateModelMatrix(ix, iy, iz);
                    bindMatrix();
                    glBindTexture(GL_TEXTURE_2D, iTexture.getTextureRef()[0]);
                    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
                }
            }
        }


        Log.d("OGLR", String.valueOf(System.currentTimeMillis() - lastTime));

    }

    private void resetModelMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }


    private void rotateModelMatrix(float angle, float x, float y) {
        Matrix.rotateM(mModelMatrix, 0, angle, x, y, 1);
    }

    private void translateModelMatrix(float x, float y, float z) {
        Matrix.translateM(mModelMatrix, 0, x, y, z);
    }

    private void scaleModelMatrix(float x, float y) {
        Matrix.scaleM(mModelMatrix, 0, x, y, 1);
    }
}
