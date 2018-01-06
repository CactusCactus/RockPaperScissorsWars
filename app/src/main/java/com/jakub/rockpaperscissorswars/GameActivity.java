package com.jakub.rockpaperscissorswars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jakub.rockpaperscissorswars.game_view.GameView;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }
}
