package com.jakub.rockpaperscissorswars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.config.Config;
import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.dao.FirebaseDAO;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;
import com.jakub.rockpaperscissorswars.widgets.LoadingScreen;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WarriorActivity extends AppCompatActivity {
    @BindView(R.id.username_label)
    TextView usernameLabel;
    @BindView(R.id.player_lvl_label)
    TextView playerLvlTv;
    @BindView(R.id.player_exp_label)
    TextView playerExpTv;
    @BindView(R.id.player_victories_label)
    TextView playerVictoriesTv;
    @BindView(R.id.player_skillpoints_label)
    TextView playerSPTv;

    @BindView(R.id.player_health_val)
    TextView playerHealthTv;
    @BindView(R.id.player_defence_val)
    TextView playerDefenceTv;
    @BindView(R.id.player_rock_val)
    TextView playerRockTv;
    @BindView(R.id.player_paper_val)
    TextView playerPaperTv;
    @BindView(R.id.player_scissors_val)
    TextView playerScissorsTv;

    @BindView(R.id.health_plus_btn)
    ImageButton healthPlusBtn;
    @BindView(R.id.defence_plus_btn)
    ImageButton defencePlusBtn;
    @BindView(R.id.rock_plus_btn)
    ImageButton rockPlusBtn;
    @BindView(R.id.paper_plus_btn)
    ImageButton paperPlusBtn;
    @BindView(R.id.scissors_plus_btn)
    ImageButton scissorsPlusBtn;
    @BindView(R.id.root_layout)
    RelativeLayout rootView;

    private User playerUser;
    private LoadingScreen loadingScreen;
    private DatabaseReference ref;
    private ValueEventListener eventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior);
        ButterKnife.bind(this);
        loadingScreen = LoadingScreen.create(this);
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
    }
    private void createDbConnection() {
        rootView.addView(loadingScreen);
        ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS);
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user != null) {
                        if (user.getEmail().equals(playerUser.getEmail())) {
                            playerUser = user;
                            toggleButtons();
                            fillFields(playerUser);
                            rootView.removeView(loadingScreen);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rootView.removeView(loadingScreen);

            }
        };
    }
    private void fillFields(User user) {
        usernameLabel.setText(user.getUsername());
        playerLvlTv.setText(String.valueOf(user.getLvl()));
        playerExpTv.setText(user.getExperience() + "/" + Utils.getExpToLvl(user.getLvl()));
        playerVictoriesTv.setText(String.valueOf(user.getVictories()));
        playerSPTv.setText(String.valueOf(user.getSkillPoints()));
        playerHealthTv.setText(String.valueOf(user.getHealth()));
        playerDefenceTv.setText(String.valueOf(user.getDefence()));
        playerRockTv.setText(String.valueOf(user.getRockVal()));
        playerPaperTv.setText(String.valueOf(user.getPaperVal()));
        playerScissorsTv.setText(String.valueOf(user.getScissorsVal()));
    }
    private void toggleButtons() {
        boolean enable = playerUser.getSkillPoints() > 0;
        healthPlusBtn.setEnabled(enable);
        defencePlusBtn.setEnabled(enable);
        rockPlusBtn.setEnabled(enable);
        paperPlusBtn.setEnabled(enable);
        scissorsPlusBtn.setEnabled(enable);
    }

    @OnClick(R.id.health_plus_btn)
    public void onHealthPlusClick() {
        playerUser.setHealth(playerUser.getHealth() + ConfigController.getConfig().getHealthIncrease());
        updateScreenAndDB();
    }
    @OnClick(R.id.defence_plus_btn)
    public void onDefencePlusClick() {
        playerUser.setDefence(playerUser.getDefence() + ConfigController.getConfig().getDefenceIncrease());
        updateScreenAndDB();
    }
    @OnClick(R.id.rock_plus_btn)
    public void onRockPlusClick() {
        playerUser.setRockVal(playerUser.getRockVal() + ConfigController.getConfig().getSkillIncrease());
        updateScreenAndDB();
    }
    @OnClick(R.id.paper_plus_btn)
    public  void onPaperPlusClick() {
        playerUser.setPaperVal(playerUser.getPaperVal() + ConfigController.getConfig().getSkillIncrease());
        updateScreenAndDB();
    }
    @OnClick(R.id.scissors_plus_btn)
    public void onScissorsPlusClick() {
        playerUser.setScissorsVal(playerUser.getScissorsVal() + ConfigController.getConfig().getSkillIncrease());
        updateScreenAndDB();
    }
    private void updateScreenAndDB() {
        deceaseSkillPoints();
        toggleButtons();
        FirebaseDAO.updatePlayer(playerUser);
    }
    private void deceaseSkillPoints() {
        playerUser.setSkillPoints(playerUser.getSkillPoints() - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(eventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ref == null || eventListener == null) {
            createDbConnection();
        }
        ref.addValueEventListener(eventListener);
    }
}
