package com.jakub.rockpaperscissorswars.config;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.constants.AppConstants;

/**
 * Created by Emil on 2018-01-10.
 */

public class ConfigController {
    private static Config config;

    public ConfigController() {
    }

    public static void syncConfig(final ConfigListener listener){
        DatabaseReference configRef = FirebaseDatabase.getInstance()
                .getReference().child(AppConstants.DB_CONFIG);
        configRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                config = dataSnapshot.getValue(Config.class);
                if(config != null) {
                    listener.onConfigReady();
                } else {
                    listener.onConfigFail();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onConfigFail();
            }
        });
    }

    public static Config getConfig() {
        return config;
    }
}
