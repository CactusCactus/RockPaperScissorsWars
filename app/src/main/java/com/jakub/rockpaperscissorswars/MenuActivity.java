package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
import com.jakub.rockpaperscissorswars.dao.FirebaseDAO;
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
    @BindView(R.id.victories_label)
    TextView victoriesLabel;

    private User playerUser;
    private LoadingScreen loadingScreen;
    private Snackbar waitingForBattleSnack;

    DatabaseReference battleDatabaseRef;
    private ValueEventListener lookingForBattleListener;
    private ValueEventListener lookingForOpponentListener;
    private ValueEventListener updatePlayerListener;

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
        lookingForBattleListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Battle battle = child.getValue(Battle.class);
                    if (battle != null && !battle.getFirstPlayer().getUsername().equals(playerUser.getUsername()) && battle.getSecondPlayer() == null) {
                        battle.setSecondPlayer(playerUser);
                        FirebaseDAO.updateBattle(battle);
                        intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(battle));
                        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
                        startBattleActivity(intent);
                        return;
                    }
                }
                Battle battle = new Battle(playerUser, null);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
                FirebaseDAO.updateBattle(battle);
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
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Battle battle = child.getValue(Battle.class);
                    if (battle != null && battle.getFirstPlayer().getUsername().equals(playerUser.getUsername()) && battle.getSecondPlayer() != null) {
                        intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(battle));
                        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerUser));
                        battleDatabaseRef.removeEventListener(this);
                        startBattleActivity(intent);
                        rootLayout.removeView(loadingScreen);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rootLayout.removeView(loadingScreen);
            }
        };
        updatePlayerListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (user.getEmail().equals(playerUser.getEmail())) {
                        playerUser = user;
                        updateUserFields();
                        rootLayout.removeView(loadingScreen);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private void init() {
        ButterKnife.bind(this);
        loadingScreen = LoadingScreen.create(this);
    }

    private void initUser() {
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        updateUserFields();
    }
    private void updateUserFields() {
        usernameLabel.setText(playerUser.getUsername());
        victoriesLabel.setText(String.valueOf(playerUser.getVictories()));
    }

    private void updatePlayer() {
        rootLayout.addView(loadingScreen);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS).child(playerUser.getUsername());
        reference.addListenerForSingleValueEvent(updatePlayerListener);
    }

    private void lookForBattle() {
        battleDatabaseRef = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
        battleDatabaseRef.addListenerForSingleValueEvent(lookingForBattleListener);
    }

    private void waitForOpponent(DatabaseReference ref) {
        rootLayout.addView(loadingScreen);
        if (waitingForBattleSnack == null) {
            waitingForBattleSnack = Snackbar.make(rootLayout, R.string.waiting_for_enemy, BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(R.string.cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDAO.deleteBattle(playerUser);
                            rootLayout.removeView(loadingScreen);
                        }
                    });

        }
        waitingForBattleSnack.show();
        Toast.makeText(this, R.string.no_battle_found, Toast.LENGTH_LONG).show();
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
    private void startRankingActivity() {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }
    private void startInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(AppConstants.FROM_MAIN_MENU, true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        if(waitingForBattleSnack != null) {
            waitingForBattleSnack.dismiss();
        }
        boolean langChanged = getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).getBoolean(AppConstants.LANG_CHANGED_MENU, false);
        if (langChanged) {
            recreate();
            getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).edit().putBoolean(AppConstants.LANG_CHANGED_MENU, false).apply();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (battleDatabaseRef != null) {
            battleDatabaseRef.removeEventListener(lookingForOpponentListener);
        }
        rootLayout.removeView(loadingScreen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BATTLE_ACTIVITY_CODE) {
            updatePlayer();
            Battle finishedBattle = Parcels.unwrap(data.getParcelableExtra(AppConstants.BATTLE_PARCEL));
            boolean isFirstPlayer = data.getBooleanExtra(AppConstants.FIRST_PLAYER_EXTRA, false);
            FirebaseDAO.deleteBattle(finishedBattle);
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
    @OnClick(R.id.ranking_btn)
    public void onRankingScreenClick() {
        startRankingActivity();
    }

    @OnClick(R.id.info_btn)
    public void onInfoClick() {
        startInfoActivity();
    }

}
