package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.constants.AttackType;
import com.jakub.rockpaperscissorswars.dao.FirebaseDAO;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;

import org.parceler.Parcels;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GameActivity extends AppCompatActivity {

    @BindView(R.id.player_rock_btn)
    ImageButton playerRockBtn;
    @BindView(R.id.player_paper_btn)
    ImageButton playerPaperBtn;
    @BindView(R.id.player_scissors_btn)
    ImageButton playerScissorsBtn;
    @BindView(R.id.quit_btn)
    ImageButton quitBtn;

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

    @BindView(R.id.player_battle_field)
    ImageView playerBattleField;
    @BindView(R.id.enemy_battle_field)
    ImageView enemyBattleField;

    @BindView(R.id.player_buttons_layout)
    LinearLayout playerButtonsLayout;
    @BindView(R.id.enemy_buttons_layout)
    LinearLayout enemyButtonsLayout;

    @BindView(R.id.main_label)
    TextView mainLabel;

    @BindView(R.id.player_health_layout)
    LinearLayout playerHealthLayout;
    @BindView(R.id.player_defence_layout)
    LinearLayout playerDefenceLayout;

    @BindView(R.id.enemy_health_layout)
    LinearLayout enemyHealthLayout;
    @BindView(R.id.enemy_defence_layout)
    LinearLayout enemyDefenceLayout;

    private Battle currentBattle;
    private DatabaseReference battleRef;
    private User playerUser;
    private User enemyUser;
    private boolean isFirstPlayer;
    private boolean movePossible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        initUsers();
        if (playerUser != null && enemyUser != null) {
            updateFieldsInit();
        }
        setDataListener();
    }

    private void initUsers() {
        currentBattle = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.BATTLE_PARCEL));
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        isFirstPlayer = playerUser.getUsername().equals(currentBattle.getFirstPlayer().getUsername());
        enemyUser = isFirstPlayer ? currentBattle.getSecondPlayer() : currentBattle.getFirstPlayer();
    }

    //Wywoywane TYLKO na poczatku bitwy
    private void updateFieldsInit() {
        playerNameLabel.setText(playerUser.getUsername());
        playerLvlLabel.setText(String.valueOf(playerUser.getLvl()));
        playerHealthVal.setText(getPlayerHealth());
        playerDefenceVal.setText(String.valueOf(playerUser.getDefence()) + "%");
        playerRockVal.setText(String.valueOf(playerUser.getRockVal()));
        playerPaperVal.setText(String.valueOf(playerUser.getPaperVal()));
        playerScissorsVal.setText(String.valueOf(playerUser.getScissorsVal()));

        enemyNameLabel.setText(enemyUser.getUsername());
        enemyLvlLabel.setText(String.valueOf(enemyUser.getLvl()));

        enemyHealthVal.setText(getEnemyHealth());
        enemyDefenceVal.setText(String.valueOf(enemyUser.getDefence()) + "%");
        enemyRockVal.setText(String.valueOf(enemyUser.getRockVal()));
        enemyPaperVal.setText(String.valueOf(enemyUser.getPaperVal()));
        enemyScissorsVal.setText(String.valueOf(enemyUser.getScissorsVal()));
        movePossible = isFirstPlayer;
        toggleButtonsLock(movePossible);
    }

    //Aktualizowanie po kazdym sygnale
    private void updateBattleField() {
        AttackType playerAttackType = isFirstPlayer ? currentBattle.getFirstPlayerMove() : currentBattle.getSecondPlayerMove();
        AttackType enemyAttackType = isFirstPlayer ? currentBattle.getSecondPlayerMove() : currentBattle.getFirstPlayerMove();
        playerBattleField.setImageDrawable(getDrawable(playerAttackType, false));
        if(enemyAttackType != null) {
            enemyBattleField.setImageDrawable(getDrawable(R.drawable.question_sign));
        } else {
            enemyBattleField.setImageDrawable(null);
        }
    }

    private void toggleButtonsLock(boolean enabled) {
        playerRockBtn.setEnabled(enabled);
        playerPaperBtn.setEnabled(enabled);
        playerScissorsBtn.setEnabled(enabled);
        playerButtonsLayout.setBackgroundColor(enabled ? getResources().getColor(R.color.very_light_gray) : getResources().getColor(R.color.dark_gray));
        enemyButtonsLayout.setBackgroundColor(!enabled ? getResources().getColor(R.color.very_light_gray) : getResources().getColor(R.color.dark_gray));
    }

    private void setDataListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE);
        battleRef = ref.child(currentBattle.getFirstPlayer().getUsername());
        battleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentBattle = dataSnapshot.getValue(Battle.class);
                if (currentBattle != null) {
                    playerUser = isFirstPlayer ? currentBattle.getFirstPlayer() : currentBattle.getSecondPlayer(); //Potrzebne?
                    enemyUser = isFirstPlayer ? currentBattle.getSecondPlayer() : currentBattle.getFirstPlayer(); //Potrzebne?
                    movePossible = isMovePossible();
                    updateBattleField();
                    toggleButtonsLock(movePossible);
                    calculateVictory();
                } else {
                    //finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isMovePossible() {
        if (currentBattle.getFirstPlayerMove() == null && currentBattle.getSecondPlayerMove() == null)
            return isFirstPlayer;
        return (isFirstPlayer && currentBattle.getFirstPlayerMove() == null) || (!isFirstPlayer && currentBattle.getSecondPlayerMove() == null);
    }

    @OnClick(R.id.player_rock_btn)
    public void onRockBtnClick() {
        if (isFirstPlayer) {
            currentBattle.setFirstPlayerMove(AttackType.ROCK);
        } else {
            currentBattle.setSecondPlayerMove(AttackType.ROCK);
        }
        FirebaseDAO.updateBattle(currentBattle);
        toggleButtonsLock(false);
    }

    @OnClick(R.id.player_paper_btn)
    public void onPaperBtnClick() {
        if (isFirstPlayer) {
            currentBattle.setFirstPlayerMove(AttackType.PAPER);
        } else {
            currentBattle.setSecondPlayerMove(AttackType.PAPER);
        }
        FirebaseDAO.updateBattle(currentBattle);
        toggleButtonsLock(false);
    }

    @OnClick(R.id.player_scissors_btn)
    public void onScissorsBtnClick() {
        if (isFirstPlayer) {
            currentBattle.setFirstPlayerMove(AttackType.SCISSORS);
        } else {
            currentBattle.setSecondPlayerMove(AttackType.SCISSORS);
        }
        FirebaseDAO.updateBattle(currentBattle);
        toggleButtonsLock(false);
    }
    @OnClick(R.id.quit_btn)
    public void onQuitBtnClick() {
        onBackPressed();
    }
    private void calculateVictory() {
        if (currentBattle.getFirstPlayerMove() != null && currentBattle.getSecondPlayerMove() != null) {
            AttackType enemyAttackType = isFirstPlayer ? currentBattle.getSecondPlayerMove() : currentBattle.getFirstPlayerMove();
            enemyBattleField.setImageDrawable(getDrawable(enemyAttackType, false));
            int result = Utils.whoWon(currentBattle.getFirstPlayerMove(), currentBattle.getSecondPlayerMove());
            displayResult(result);
            setAndDisplayDamage(result);
            playerHealthVal.setText(getPlayerHealth());
            enemyHealthVal.setText(getEnemyHealth());
            mainLabel.setVisibility(View.VISIBLE);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentBattle.setFirstPlayerMove(null);
                    currentBattle.setSecondPlayerMove(null);
                    mainLabel.setVisibility(View.GONE);
                    FirebaseDAO.updateBattle(currentBattle);
                    resetHealthAndDefenceColors();
                }
            }, 3000);
            boolean gameOver = false;
            if(currentBattle.getFirstPlayerHp() <=0 && currentBattle.getSecondPlayerHp() <= 0) {
                mainLabel.setText(R.string.both_died);
                toggleButtonsLock(false);
                gameOver = true;
            } else if(currentBattle.getFirstPlayerHp() <= 0) {
                mainLabel.setText(currentBattle.getFirstPlayer().getUsername() +  " " +getString(R.string.dies));
                toggleButtonsLock(false);
                gameOver = true;
                currentBattle.setWinner(currentBattle.getSecondPlayer());
                currentBattle.setLoser(currentBattle.getFirstPlayer());
            } else if (currentBattle.getSecondPlayerHp() <= 0) {
                mainLabel.setText(currentBattle.getSecondPlayer().getUsername() +  " " +getString(R.string.dies));
                toggleButtonsLock(false);
                gameOver = true;
                currentBattle.setWinner(currentBattle.getFirstPlayer());
                currentBattle.setLoser(currentBattle.getSecondPlayer());
            }
            if(gameOver) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra(AppConstants.BATTLE_PARCEL, Parcels.wrap(currentBattle));
                        intent.putExtra(AppConstants.FIRST_PLAYER_EXTRA, isFirstPlayer);
                        GameActivity.this.setResult(441, intent);
                        GameActivity.this.finish();
                    }
                }, 7000);
            }
        }
    }
    private void displayResult(int result) {
        User firstPlayer = currentBattle.getFirstPlayer();
        User secondPlayer = currentBattle.getSecondPlayer();
        if (result == 1) {
            if(isFirstPlayer) playerBattleField.setImageDrawable(getDrawable(currentBattle.getFirstPlayerMove(), true));
            else enemyBattleField.setImageDrawable(getDrawable(currentBattle.getFirstPlayerMove(), true));
            String s = firstPlayer.getUsername() + " " + getString(R.string.won) + "!";
            mainLabel.setText(s);
        } else if (result == -1) {
            if(!isFirstPlayer) playerBattleField.setImageDrawable(getDrawable(currentBattle.getSecondPlayerMove(), true));
            else enemyBattleField.setImageDrawable(getDrawable(currentBattle.getSecondPlayerMove(), true));
            String s = secondPlayer.getUsername() + " " + getString(R.string.won) + "!";
            mainLabel.setText(s);
        } else {
            mainLabel.setText(R.string.draw);
        }
    }
    private void setAndDisplayDamage(int result) {
        Random random = new Random();
        int randomNum = random.nextInt(100) + 1;
        if(result == 1) {
            if(randomNum > currentBattle.getSecondPlayer().getDefence()) {
                int damage = Utils.getDamage(currentBattle.getFirstPlayer(), currentBattle.getFirstPlayerMove());
                currentBattle.setSecondPlayerHp(currentBattle.getSecondPlayerHp() - damage);
                displayDamage(result);
            } else {
                displayBlock(1);
            }
        } else if (result == -1) {
            if(randomNum > currentBattle.getFirstPlayer().getDefence()) {
                int damage = Utils.getDamage(currentBattle.getSecondPlayer(), currentBattle.getSecondPlayerMove());
                currentBattle.setFirstPlayerHp(currentBattle.getFirstPlayerHp() - damage);
                displayDamage(result);
            } else {
                displayBlock(-1);
            }
        } else {
            int firstPlayerDamage = Utils.getDamage(currentBattle.getFirstPlayer(), currentBattle.getFirstPlayerMove());
            int secondPlayerDamage = Utils.getDamage(currentBattle.getSecondPlayer(), currentBattle.getSecondPlayerMove());
            firstPlayerDamage = (int) ((double) firstPlayerDamage / 2);
            secondPlayerDamage = (int) ((double) secondPlayerDamage / 2);
            if(randomNum > currentBattle.getSecondPlayer().getDefence()) {
                currentBattle.setSecondPlayerHp(currentBattle.getSecondPlayerHp() - firstPlayerDamage);
                displayDamage(1);
            } else {
                displayBlock(1);
            }
            if(randomNum > currentBattle.getFirstPlayer().getDefence()) {
                currentBattle.setFirstPlayerHp(currentBattle.getFirstPlayerHp() - secondPlayerDamage);
                displayDamage(-1);
            } else {
                displayBlock(-1);
            }
        }
    }
    private void displayDamage(int result) {
        switch (result) {
            case 1:
                if(isFirstPlayer) toggleEnemyHealthColor(true);
                else togglePlayerHealthColor(true);
                break;
            case -1:
                if(!isFirstPlayer) toggleEnemyHealthColor(true);
                else togglePlayerHealthColor(true);
                break;
            case 0:
                togglePlayerHealthColor(true);
                toggleEnemyHealthColor(true);
                break;
        }
    }
    private void displayBlock(int result) {
        switch (result) {
            case 1:
                if(isFirstPlayer) toggleEnemyDefenceColor(true);
                else togglePlayerDefenceColor(true);
                break;
            case -1:
                if(!isFirstPlayer) toggleEnemyDefenceColor(true);
                else togglePlayerDefenceColor(true);
                break;
            case 0:
                togglePlayerDefenceColor(true);
                toggleEnemyDefenceColor(true);
                break;
        }
    }
    private Drawable getDrawable(AttackType type, boolean red) {
        if (type == null) return null;
        switch (type) {
            case ROCK:
                return red ? getDrawable(R.drawable.rock_red) : getDrawable(R.drawable.rock);
            case PAPER:
                return red ? getDrawable(R.drawable.paper_red) : getDrawable(R.drawable.paper);
            case SCISSORS:
                return red ? getDrawable(R.drawable.scissors_red) : getDrawable(R.drawable.scissors);
            default:
                return null;
        }
    }
    private void togglePlayerHealthColor(boolean red) {
        playerHealthLayout.setBackgroundColor(red ? getResources().getColor(R.color.light_red) : getResources().getColor(R.color.white));
    }
    private void toggleEnemyHealthColor(boolean red) {
        enemyHealthLayout.setBackgroundColor(red ? getResources().getColor(R.color.light_red) : getResources().getColor(R.color.white));
    }
    private void togglePlayerDefenceColor(boolean red) {
        playerDefenceLayout.setBackgroundColor(red ? getResources().getColor(R.color.light_red) : getResources().getColor(R.color.white));
    }
    private void toggleEnemyDefenceColor(boolean red) {
        enemyDefenceLayout.setBackgroundColor(red ? getResources().getColor(R.color.light_red) : getResources().getColor(R.color.white));
    }
    private void resetHealthAndDefenceColors() {
        togglePlayerHealthColor(false);
        togglePlayerDefenceColor(false);
        toggleEnemyHealthColor(false);
        toggleEnemyDefenceColor(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDAO.deleteBattle(currentBattle);
    }
    private String getPlayerHealth() {
        return String.valueOf(isFirstPlayer ? currentBattle.getFirstPlayerHp() : currentBattle.getSecondPlayerHp() + "/" +
                String.valueOf(isFirstPlayer ? currentBattle.getFirstPlayer().getHealth() : currentBattle.getSecondPlayer().getHealth()));
    }
    private String getEnemyHealth() {
        return String.valueOf(isFirstPlayer ? currentBattle.getSecondPlayerHp() : currentBattle.getFirstPlayerHp()) + "/" +
                String.valueOf(isFirstPlayer ? currentBattle.getSecondPlayer().getHealth() : currentBattle.getFirstPlayer().getHealth());
    }
}
