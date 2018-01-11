package com.jakub.rockpaperscissorswars;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.dao.FirebaseDAO;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionsActivity extends AppCompatActivity {
    @BindView(R.id.version_label)
    TextView versionLabel;
    @BindView(R.id.lang_spinner)
    Spinner langSpinner;
    User playerUser;
    boolean firstSelect = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        playerUser = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
        versionLabel.setText(Utils.getAppVersion(this));
        initLangSpinner();
    }
    private void initLangSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.lang_array));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(dataAdapter);
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!firstSelect) {
                    String lang = getResources().getStringArray(R.array.lang_codes_array)[position];
                    Utils.setLocale(lang, getApplicationContext());
                    Snackbar.make(langSpinner, R.string.quit_to_apply, Snackbar.LENGTH_LONG).show();
                    getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).edit().putBoolean(AppConstants.LANG_CHANGED_MENU, true).apply();
                    getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).edit().putBoolean(AppConstants.LANG_CHANGED_SIGNIN, true).apply();
                    getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE).edit().putString(AppConstants.USER_LANG, lang).apply();
                }
                firstSelect = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.delete_warrior_btn)
    public void onDeleteWarriorClick() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.delete_warrior))
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWarrior();
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
                        deleteWarrior();
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
    private void deleteWarrior() {
        FirebaseDAO.deletePlayer(playerUser);
    }
}
