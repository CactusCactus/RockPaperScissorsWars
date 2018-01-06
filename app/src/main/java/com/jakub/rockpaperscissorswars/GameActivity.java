package com.jakub.rockpaperscissorswars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;


public class GameActivity extends AppCompatActivity {

    @BindView(R.id.player_rock_btn)
    Button playerRockBtn;
    @BindView(R.id.player_paper_btn)
    Button playerPaperBtn;
    @BindView(R.id.player_scissors_btn)
    Button playerScissorsBtn;
    @BindView(R.id.quit_btn)
    Button quitBtn;

    @BindView(R.id.player_rock_val)
    TextView playerRockVal;
    @BindView(R.id.player_paper_val)
    TextView playerPaperVal;
    @BindView(R.id.player_scissors_val)
    TextView playerScissorsVal;
    @BindView(R.id.enemy_rock_val)
    TextView enemyRockVal;
    @BindView(R.id.enemy_paper_val)
    TextView enemyPaperVal;
    @BindView(R.id.enemy_scissors_val)
    TextView enemyScissorsVal;

    @BindView(R.id.player_name_label)
    TextView playerNameLabel;
    @BindView(R.id.enemy_name_label)
    TextView enemyNameLabel;
    @BindView(R.id.player_lvl_label)
    TextView playerLvlLabel;
    @BindView(R.id.enemy_lvl_label)
    TextView enemyLvlLabel;
    @BindView(R.id.player_health_val)
    TextView playerHealthVal;
    @BindView(R.id.enemy_health_val)
    TextView enemyHealthVal;
    @BindView(R.id.player_defence_val)
    TextView playerDefenceVal;
    @BindView(R.id.enemy_defence_val)
    TextView enemyDefenceVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
