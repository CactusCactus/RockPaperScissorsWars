package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.constants.AttackType;
import com.jakub.rockpaperscissorswars.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CharacterCreationActivity extends AppCompatActivity {
    //TODO sprawdzaj istnienie nicku
    private static final String TAG = CharacterCreationActivity.class.getCanonicalName();
    @BindView(R.id.email_label)
    TextView emailLabel;
    @BindView(R.id.username_input)
    EditText usernameInput;
    @BindView(R.id.rock_btn)
    ImageButton rockBtn;
    @BindView(R.id.paper_btn)
    ImageButton paperBtn;
    @BindView(R.id.scissors_btn)
    ImageButton scissorsBtn;


    private AttackType selectedType;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            emailLabel.setText(user.getDisplayName());
        }
    }

    @OnClick(R.id.rock_btn)
    public void rockBtnClick() {
        selectedType = AttackType.ROCK;
        toggleButtonColors(AttackType.ROCK);
    }

    @OnClick(R.id.paper_btn)
    public void paperBtnClick() {
        selectedType = AttackType.PAPER;
        toggleButtonColors(AttackType.PAPER);
    }

    @OnClick(R.id.scissors_btn)
    public void scissorsBtnClick() {
        selectedType = AttackType.SCISSORS;
        toggleButtonColors(AttackType.SCISSORS);
    }

    @OnClick(R.id.confirm_btn)
    public void onConfirmClick() {
        String username = usernameInput.getText().toString();
        if (selectedType != null && !username.isEmpty() && user.getEmail() != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();
            final User newUser = new User(user.getEmail(), username,
                    getStat(AttackType.ROCK), getStat(AttackType.PAPER), getStat(AttackType.SCISSORS));
            mDatabase.child(AppConstants.DB_USERS).child(username).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(CharacterCreationActivity.this, InfoActivity.class);
                            intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(newUser));
                            startActivity(intent);
                        }
                    }
                });
        } else {
            Snackbar.make(usernameInput, R.string.select_name_and_weapon, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private int getStat(AttackType type) {
        if (type.equals(selectedType))
            return ConfigController.getConfig().getBaseStat() + ConfigController.getConfig().getChosenWeaponBonus();
        else return ConfigController.getConfig().getBaseStat();
    }

    private void toggleButtonColors(AttackType type) {
        switch (type) {
            case ROCK:
                rockBtn.setImageResource(R.drawable.rock_red);
                paperBtn.setImageResource(R.drawable.paper);
                scissorsBtn.setImageResource(R.drawable.scissors);
                break;
            case PAPER:
                rockBtn.setImageResource(R.drawable.rock);
                paperBtn.setImageResource(R.drawable.paper_red);
                scissorsBtn.setImageResource(R.drawable.scissors);
                break;
            case SCISSORS:
                rockBtn.setImageResource(R.drawable.rock);
                paperBtn.setImageResource(R.drawable.paper);
                scissorsBtn.setImageResource(R.drawable.scissors_red);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }
}
