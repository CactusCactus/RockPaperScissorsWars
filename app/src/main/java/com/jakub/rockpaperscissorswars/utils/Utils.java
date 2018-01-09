package com.jakub.rockpaperscissorswars.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jakub.rockpaperscissorswars.constants.AttackType;
import com.jakub.rockpaperscissorswars.models.User;

/**
 * Created by Emil on 2018-01-07.
 */

public class Utils {
    public static int whoWon(AttackType first, AttackType second) {
        switch (first) {
            case ROCK:
                switch (second) {
                    case ROCK:
                        return 0;
                    case PAPER:
                        return -1;
                    case SCISSORS:
                        return 1;
                    default:
                        return 0;
                }
            case PAPER:
                switch (second) {
                    case ROCK:
                        return 1;
                    case PAPER:
                        return 0;
                    case SCISSORS:
                        return -1;
                    default:
                        return 0;
                }
            case SCISSORS:
                switch (second) {
                    case ROCK:
                        return -1;
                    case PAPER:
                        return 1;
                    case SCISSORS:
                        return 0;
                    default:
                        return 0;
                }
            default:
                return 0;
        }
    }
    public static int getDamage(User user, AttackType attackType) {
        switch (attackType) {
            case ROCK: return user.getRockVal();
            case PAPER: return user.getPaperVal();
            case SCISSORS: return user.getScissorsVal();
            default: return 0;
        }
    }
    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
