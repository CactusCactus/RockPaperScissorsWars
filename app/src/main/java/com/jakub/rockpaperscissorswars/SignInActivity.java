package com.jakub.rockpaperscissorswars;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.config.ConfigListener;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;
import com.jakub.rockpaperscissorswars.widgets.LoadingScreen;

import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.logged_in_label)
    TextView loggedInLabel;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.start_btn)
    Button startButton;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = SignInActivity.class.getCanonicalName();

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        loadingScreen = new LoadingScreen(this);
        initSignIn();
    }

    private void setLanguage() {
        String lang = getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).getString(AppConstants.USER_LANG, Locale.getDefault().getLanguage());
        Utils.setLocale(lang, this);
    }
    private void initSignIn() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null) {
            googleSignInClient.signOut();
        }
        toggleStartBtn(firebaseUser != null);
        toggleLoggedIn(firebaseUser != null);
        startButton.setEnabled(firebaseUser != null);
    }
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Snackbar.make(startButton, R.string.error_auth, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signInGoogle();
                break;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toggleLoggedIn(true);
                            toggleStartBtn(true);
                            startButton.setEnabled(true);

                        } else {
                            googleSignInClient.signOut();
                            toggleLoggedIn(false);
                            Snackbar.make(startButton, R.string.error_auth, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void completeLogin(final FirebaseUser firebaseUser) {
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(SignInActivity.this, CharacterCreationActivity.class);
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if(user != null) {
                        if (user.getEmail().equals(firebaseUser.getEmail())) {
                            intent = new Intent(SignInActivity.this, MenuActivity.class);
                            intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(user));
                            break;
                        }
                    }
                }
                final Intent finalIntent = intent;
                ConfigController.syncConfig(new ConfigListener() {
                    @Override
                    public void onConfigReady() {
                        startActivity(finalIntent);
                        rootLayout.removeView(loadingScreen);
                    }

                    @Override
                    public void onConfigFail() {
                        Snackbar.make(startButton, R.string.error_db_connection, Snackbar.LENGTH_SHORT).show();
                        rootLayout.removeView(loadingScreen);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(startButton, R.string.error_db_connection, Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void toggleLoggedIn(boolean loggedIn) {
        loggedInLabel.setText(loggedIn ? getString(R.string.logged_in) : getString(R.string.not_logged_in));
        loggedInLabel.setTextColor(loggedIn ? getResources().getColor(R.color.light_red) : getResources().getColor(R.color.light_gray));
    }
    private void toggleStartBtn(boolean loggedIn) {
        startButton.setVisibility(loggedIn ? View.VISIBLE : View.GONE);
        signInButton.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean langChanged = getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).getBoolean(AppConstants.LANG_CHANGED_SIGNIN, false);
        if(langChanged) {
            recreate();
            getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).edit().putBoolean(AppConstants.LANG_CHANGED_SIGNIN, false).apply();
        }
    }

    @OnClick(R.id.quit_btn)
    public void onQuitClick() {
        new AlertDialog.Builder(this).setTitle(R.string.log_out)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        googleSignInClient.signOut();
                        toggleLoggedIn(false);
                        toggleStartBtn(false);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();

    }
    @OnClick(R.id.start_btn)
    public void onStartClick() {
        if (firebaseUser != null) {
            rootLayout.addView(loadingScreen);
            completeLogin(firebaseUser);
        } else {
            firebaseUser = mAuth.getCurrentUser();
        }
    }
}
