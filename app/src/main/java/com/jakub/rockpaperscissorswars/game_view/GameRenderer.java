package com.jakub.rockpaperscissorswars.game_view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Emil on 2018-01-05.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    //Matryce
    private final float[] matrixProjection = new float[16];
    private final float[] matrixView = new float[16];
    private final float[] matrixProjectionAndView = new float[16];
    //Zmienne geometryczne
    public static float vertices[];
    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    //Rozdzielczość przypisywana w @onSurfaceChange
    private float screenWidth = 1280;
    private float screenHeight = 768;

    private Context context;
    private long lastTime; //czas ostatniego renderowania
    private int program;

    public GameRenderer(Context context) {
        this.context = context;
        lastTime = System.currentTimeMillis();
    }

    public void onPause() {

    }

    public void onResume() {
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Create the triangle
        setupTriangle(); //TODO zmiana

        // wyczyszcznie kolorów przez zamianę na czarny
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Tworzenie shaderów
        int vertexShader = GraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, GraphicTools.vs_SolidColor);
        int fragmentShader = GraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, GraphicTools.fs_SolidColor);

        GraphicTools.sp_SolidColor = GLES20.glCreateProgram();             // Tworznie pustego programu OpenGL
        GLES20.glAttachShader(GraphicTools.sp_SolidColor, vertexShader);   // Dodaj shader wierzchołków
        GLES20.glAttachShader(GraphicTools.sp_SolidColor, fragmentShader); // Dodaj shader fragmentów
        GLES20.glLinkProgram(GraphicTools.sp_SolidColor);                  // Utwórz program wykonawczy

        // Ustaw program shaderowy
        GLES20.glUseProgram(GraphicTools.sp_SolidColor);
    }

    public void setupTriangle() {
        // Tworzymy wierzchołki 2 trójkątów (kwadrat)
        vertices = new float[]
                {
                    10.0f, 200f, 0.0f,
                    10.0f, 100f, 0.0f,
                    100f, 100f, 0.0f,
                    100f, 200f, 0.0f,
                };
        indices = new short[]{0, 1, 2, 0, 2, 3}; //Kolejność renderowania wierzchołków

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Przypisanie wartości wielkości ekranu
        screenWidth = width;
        screenHeight = height;

        // dostosowanie Viewportu do całego ekranu
        GLES20.glViewport(0, 0, (int) screenWidth, (int) screenHeight);

        // Wyczyszczenie matryc
        for (int i = 0; i < 16; i++) {
            matrixProjection[i] = 0.0f;
            matrixView[i] = 0.0f;
            matrixProjectionAndView[i] = 0.0f;
        }

        // Ustawienie wysokości i szerokości dla translaci spiritów
        Matrix.orthoM(matrixProjection, 0, 0f, width, 0.0f, height, 0, 50);

        // Ustawienie kamery
        Matrix.setLookAtM(matrixView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Kalkulacja ostatniej matrycy
        Matrix.multiplyMM(matrixProjectionAndView, 0, matrixProjection, 0, matrixView, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        long now = System.currentTimeMillis();
        //Sprawdzamy czy wszystko się zgadza czasowo
        if (lastTime > now) return;
        long elapsed = now - lastTime; //Czas renderowania ostatniej klatki
        //Właściwe renderowanie
        render(matrixProjectionAndView);
        //Odświeżam czas
        lastTime = now;
    }

    private void render(float[] m) {
        //Czyszczę ekran i buffer głebi ustawiam kolor na czarny
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(GraphicTools.sp_SolidColor, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(GraphicTools.sp_SolidColor, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
