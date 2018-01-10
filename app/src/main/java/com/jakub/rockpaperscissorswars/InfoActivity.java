package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends AppCompatActivity {

    User playerCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        playerCharacter = Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.PLAYER_PARCEL));
    }

    @OnClick(R.id.confirm_btn)
    public void onConfirmClick() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(AppConstants.PLAYER_PARCEL, Parcels.wrap(playerCharacter));
        startActivity(intent);
    }
}
