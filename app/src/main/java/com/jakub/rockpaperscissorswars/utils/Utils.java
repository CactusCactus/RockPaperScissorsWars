package com.jakub.rockpaperscissorswars.utils;

import com.jakub.rockpaperscissorswars.constants.AttackType;

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
}
