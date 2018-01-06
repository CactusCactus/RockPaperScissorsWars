package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.start_game_btn)
    Button startGameBtn;
    @BindView(R.id.warrior_screen_btn)
    Button warriorScreenBtn;
    @BindView(R.id.options_btn)
    Button optionsBtn;
    @BindView(R.id.username)
    TextView usernameLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
    }
    private void init() {
        ButterKnife.bind(this);
        startGameBtn.setOnClickListener(this);
        warriorScreenBtn.setOnClickListener(this);
        optionsBtn.setOnClickListener(this);
        initFirebaseUser();
    }
    private void initFirebaseUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        usernameLabel.setText(user.getEmail());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game_btn:
                startGameActivity();
                break;
            case R.id.warrior_screen_btn:
                startWarriorActivity();
                break;
            case R.id.options_btn:
                startOptionsActivity();
                break;
        }
    }
    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    private void startWarriorActivity() {
        Intent intent = new Intent(this, WarriorActivity.class);
        startActivity(intent);
    }
    private void startOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
