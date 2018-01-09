package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.dialogs.AftermatchDialog;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.widgets.LoadingScreen;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

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
    private LoadingScreen loadingScreen;

    DatabaseReference battleDatabaseRef;
    private ValueEventListener lookingForBattleListener;
    private ValueEventListener lookingForOpponentListener;

    private static final int BATTLE_ACTIVITY_CODE = 1902;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initListeners();
        init();
        initUser();
    }
    private void initListeners() {
         lookingForBattleListener= new ValueEventListener() {
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
                        startBattleActivity(intent);
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
         lookingForOpponentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Battle battle = child.getValue(Battle.class);
                    if(battle != null && battle.getFirstPlayer().getUsername().equals(playerUser.getUsername()) && battle.getSecondPlayer() != null) {
                        intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(battle));
                        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
                        battleDatabaseRef.removeEventListener(this);
                        startBattleActivity(intent);
                    }
                }
                rootLayout.removeView(loadingScreen);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rootLayout.removeView(loadingScreen);
            }
        };
    }
    private void init() {
        ButterKnife.bind(this);
        loadingScreen = LoadingScreen.create(this);
    }
    private void initUser() {
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        usernameLabel.setText(playerUser.getUsername());
    }

    private void lookForBattle() {
        battleDatabaseRef = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
        battleDatabaseRef.addListenerForSingleValueEvent(lookingForBattleListener);
    }
    private void waitForOpponent(DatabaseReference ref) {
        rootLayout.addView(loadingScreen);
        Toast.makeText(this, "No battle found, creating a new one", Toast.LENGTH_LONG).show();
        battleDatabaseRef.addValueEventListener(lookingForOpponentListener);
    }
    private void startWarriorActivity() {
        Intent intent = new Intent(this, WarriorActivity.class);
        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
        startActivity(intent);
    }
    private void startOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
        startActivity(intent);
    }
    private void startBattleActivity(Intent intent) {
        startActivityForResult(intent, BATTLE_ACTIVITY_CODE);
    }
    private void startInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(battleDatabaseRef != null) {
            battleDatabaseRef.removeEventListener(lookingForOpponentListener);
        }
        rootLayout.removeView(loadingScreen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BATTLE_ACTIVITY_CODE) {
            Battle finishedBattle = Parcels.unwrap(data.getParcelableExtra(AppConstants.BATTLE_PARCEL));
            boolean isFirstPlayer = data.getBooleanExtra(AppConstants.FIRST_PLAYER_EXTRA, false);
            battleDatabaseRef.child(finishedBattle.getFirstPlayer().getUsername()).removeValue();
            AftermatchDialog.create(this, finishedBattle, isFirstPlayer).show();

        }
    }
    @OnClick(R.id.start_game_btn)
    public void onStartGameClick() {
        lookForBattle();

    }
    @OnClick(R.id.warrior_screen_btn)
    public void onWarriorScreenClick() {
        startWarriorActivity();

    }
    @OnClick(R.id.options_btn)
    public void onOptionsScreenClick() {
        startOptionsActivity();

    }
    @OnClick(R.id.info_btn)
    public void onInfoClick() {
        startInfoActivity();
    }

}
