package com.jakub.rockpaperscissorswars;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionsActivity extends AppCompatActivity {
    @BindView(R.id.version_label)
    TextView versionLabel;

    User playerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        versionLabel.setText(Utils.getAppVersion(this));
    }
    @OnClick(R.id.delete_warrior_btn)
    public void onDeleteWarriorClick() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.delete_warrior))
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS).child(playerUser.getUsername());
                        ref.removeValue();
                        Intent intent = new Intent(OptionsActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();

    }
    @OnClick(R.id.delete_account_btn)
    public void onDeleteAccountBtn() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.delete_account))
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                        Intent intent = new Intent(OptionsActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();

    }

}
