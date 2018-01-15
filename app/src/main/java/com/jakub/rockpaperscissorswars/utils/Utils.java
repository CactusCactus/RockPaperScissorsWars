package com.jakub.rockpaperscissorswars.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.jakub.rockpaperscissorswars.config.Config;
import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.constants.AttackType;
import com.jakub.rockpaperscissorswars.constants.Result;
import com.jakub.rockpaperscissorswars.models.User;

import java.util.Locale;

/**
 * Created by Emil on 2018-01-07.
 */

public class Utils {
    public static Result whoWon(AttackType first, AttackType second) {
        switch (first) {
            case ROCK:
                switch (second) {
                    case ROCK:
                        return Result.DRAW;
                    case PAPER:
                        return Result.LOSE;
                    case SCISSORS:
                        return Result.WIN;
                    default:
                        return Result.DRAW;
                }
            case PAPER:
                switch (second) {
                    case ROCK:
                        return Result.WIN;
                    case PAPER:
                        return Result.DRAW;
                    case SCISSORS:
                        return Result.LOSE;
                    default:
                        return Result.DRAW;
                }
            case SCISSORS:
                switch (second) {
                    case ROCK:
                        return Result.LOSE;
                    case PAPER:
                        return Result.WIN;
                    case SCISSORS:
                        return Result.DRAW;
                    default:
                        return Result.DRAW;
                }
            default:
                return Result.DRAW;
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
    public static void setLocale(String lang, Context context) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale(lang));
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
    public static int getExpToLvl(int lvl) {
        Config config = ConfigController.getConfig();
        return (int) (Math.pow(lvl, config.getLvlUpPower()) * config.getExpToLvlRatio());
    }
}
