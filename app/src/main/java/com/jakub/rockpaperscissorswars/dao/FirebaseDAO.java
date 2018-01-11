package com.jakub.rockpaperscissorswars.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;

import java.util.List;

/**
 * Created by Emil on 2018-01-11.
 */

public class FirebaseDAO {
    public static void updatePlayer(User player) {
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS).child(player.getUsername()).setValue(player);
    }
    public static void updateBattle(Battle battle) {
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE).child(battle.getFirstPlayer().getUsername()).setValue(battle);
    }
    public static void deletePlayer(User player) {
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS).child(player.getUsername()).removeValue();
    }
    public static void deleteBattle(Battle battle) {
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE).child(battle.getFirstPlayer().getUsername()).removeValue();
    }
    public static void deleteBattle(User player) {
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_BATTLE).child(player.getUsername()).removeValue();
    }
}
