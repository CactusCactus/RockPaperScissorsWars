package com.jakub.rockpaperscissorswars.game_view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jakub.rockpaperscissorswars.R;

/**
 * Created by Emil on 2018-01-05.
 */

public class GameView extends GLSurfaceView {
    GameRenderer renderer;
    public GameView(Context context) {
        super(context);
        //setBackgroundResource(R.drawable.background_tablecloth); //TODO z jakiegoś powodu nic nie wyświetla z tłem
        //Ustawiam kontekst OpenGL ES 2.0
        setEGLContextClientVersion(2);
        //Ustawiam renderer do rysowania
        renderer = new GameRenderer(context);
        setRenderer(renderer);
        //Renderuj bez przerwy
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //Ustaw tło
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }
}
