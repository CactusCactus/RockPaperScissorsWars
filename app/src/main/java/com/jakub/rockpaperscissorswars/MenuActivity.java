package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;

import org.parceler.Parcels;

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
    @BindView(R.id.main_menu_layout)
    RelativeLayout rootLayout;


    private User playerUser;
    DatabaseReference battleDatabaseRef;
    private ValueEventListener lookingForBattleListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            final Intent intent = new Intent(MenuActivity.this, GameActivity.class);
            for (DataSnapshot child: dataSnapshot.getChildren()) {
                Battle battle = child.getValue(Battle.class);
                if(battle != null && !battle.getFirstPlayer().getUsername().equals(playerUser.getUsername())  && battle.getSecondPlayer() == null) {
                    battle.setSecondPlayer(playerUser);
                    FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE)
                            .child(battle.getFirstPlayer().getUsername()).setValue(battle);
                    intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(battle));
                    intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
                    startActivity(intent);
                    return;
                }
            }
            Battle battle = new Battle(playerUser, null);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
            ref.child(playerUser.getUsername()).setValue(battle);
            waitForOpponent(ref);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener lookingForOpponentListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            final Intent intent = new Intent(MenuActivity.this, GameActivity.class);
            for(DataSnapshot child : dataSnapshot.getChildren()) {
                Battle battle = child.getValue(Battle.class);
                if(battle != null && battle.getFirstPlayer().getUsername().equals(playerUser.getUsername()) && battle.getSecondPlayer() != null) {
                    intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(battle));
                    intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
                    battleDatabaseRef.removeEventListener(this);
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        initUser();
    }
    private void init() {
        ButterKnife.bind(this);
        startGameBtn.setOnClickListener(this);
        warriorScreenBtn.setOnClickListener(this);
        optionsBtn.setOnClickListener(this);
    }
    private void initUser() {
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        usernameLabel.setText(playerUser.getUsername());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game_btn:
                lookForBattle();
                break;
            case R.id.warrior_screen_btn:
                startWarriorActivity();
                break;
            case R.id.options_btn:
                startOptionsActivity();
                break;
        }
    }
    private void lookForBattle() {
        battleDatabaseRef = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
        battleDatabaseRef.addListenerForSingleValueEvent(lookingForBattleListener);
    }
    private void waitForOpponent(DatabaseReference ref) {
        Toast.makeText(this, "No battle found, creating a new one", Toast.LENGTH_LONG).show();
        //rootLayout.addView(LoadingScreen.create(this)); //TODO pozniej przy tym poszperam
        battleDatabaseRef.addValueEventListener(lookingForOpponentListener);
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
