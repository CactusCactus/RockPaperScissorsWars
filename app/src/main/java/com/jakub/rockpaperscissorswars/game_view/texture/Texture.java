package com.jakub.rockpaperscissorswars.game_view.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Emil on 2018-01-05.
 */

public class Texture {
    private FloatBuffer vertBuffer;
    private FloatBuffer texBuffer;
    private ByteBuffer indexBuffer;
    private int[] textures = new int[1];
    private float vertices[] = {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
    };
    private float texture[];
    private byte indices[] = {
            0, 1, 2,
            0, 2, 3
    };

    public Texture(int[] textures) {
        this.textures = textures;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertBuffer = byteBuffer.asFloatBuffer();
        vertBuffer.put(vertices);
        vertBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        texBuffer = byteBuffer.asFloatBuffer();
        texBuffer.put(texture);
        texBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void loadTexture(GL10 gl, int texture, Context context) {
        InputStream imgStream = context.getResources().openRawResource(texture);
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeStream(imgStream);
        try {
            imgStream.close();
            imgStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
    public void draw(GL10 gl){
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
            gl.glFrontFace(GL10.GL_CCW);
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glCullFace(GL10.GL_BACK);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuffer);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
            GL10.GL_UNSIGNED_BYTE, indexBuffer);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL10.GL_CULL_FACE);
    }
}
