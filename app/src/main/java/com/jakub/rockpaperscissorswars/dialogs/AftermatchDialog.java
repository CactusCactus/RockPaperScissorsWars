package com.jakub.rockpaperscissorswars.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.jakub.rockpaperscissorswars.R;
import com.jakub.rockpaperscissorswars.config.Config;
import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.constants.Result;
import com.jakub.rockpaperscissorswars.models.Battle;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Emil on 2018-01-08.
 */

public class AftermatchDialog extends Dialog {

    @BindView(R.id.outcome_label)
    TextView outcomeLabel;
    @BindView(R.id.current_level_label)
    TextView currentLvlTv;
    @BindView(R.id.next_level_label)
    TextView nextLevelTv;
    @BindView(R.id.lvl_progress_bar)
    ProgressBar levelProgressBar;

    private AftermatchDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public static AftermatchDialog create(Context context, Battle battle, boolean isFirstPlayer) {
        AftermatchDialog dialog = new AftermatchDialog(context);
        dialog.fillData(battle, isFirstPlayer);
        return dialog;
    }

    private void init() {
        View rootView = getLayoutInflater().inflate(R.layout.aftermatch_dialog, null);
        setContentView(rootView);
        ButterKnife.bind(this);
    }

    private void fillData(Battle battle, boolean isFirstPlayer) {
        User player = isFirstPlayer ? battle.getFirstPlayer() : battle.getSecondPlayer();
        User enemy = isFirstPlayer ? battle.getSecondPlayer() : battle.getFirstPlayer();

        Result result = isPlayerAWinner(battle, player);
        switch (result) {
            case WIN:
                outcomeLabel.setText(R.string.win);
                calculateAndDisplayExpGain(player, enemy, false);
                break;
            case LOSE:
                outcomeLabel.setText(R.string.defeat);
                break;
            case DRAW:
                outcomeLabel.setText(R.string.draw);
                calculateAndDisplayExpGain(player, enemy, true);
                break;
        }

    }

    private void calculateAndDisplayExpGain(final User player, final User enemy, boolean isDraw) {
        Config config = ConfigController.getConfig();
        int startExp = player.getExperience();
        int increase = player.getExperience() + enemy.getLvl() * config.getExpToLvlRatio();
        if (isDraw) increase = (int) ((double) increase / 2);
        displayExp(startExp, increase, player.getLvl());

        int gainExp = enemy.getLvl() * config.getExpToLvlRatio();
        int max = (int) Math.pow(player.getLvl(), config.getLvlUpPower()) * config.getExpToLvlRatio();
        int newLvl = player.getLvl();
        int newExp = player.getExperience() + gainExp;
        while (newExp >= max) {
            newExp = newExp - max;
            newLvl++;
            max = Utils.getExpToLvl(newLvl);
        }
        player.setSkillPoints(player.getSkillPoints() + ((newLvl - player.getLvl()) * config.getSkillPointsOnLvlUp()));
        player.setLvl(newLvl);
        player.setExperience(newExp);
        player.setVictories(player.getVictories() + 1);
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS).child(player.getUsername()).setValue(player);
    }

    private void displayExp(final int startExp, final int increase, int playerLvl) {
        Config config = ConfigController.getConfig();
        final int max = Utils.getExpToLvl(playerLvl);
        currentLvlTv.setText(String.valueOf(playerLvl));
        nextLevelTv.setText(String.valueOf(playerLvl + 1));
        int progressTo = increase;
        boolean lvlUp = false;
        if (increase >= max) {
            lvlUp = true;
            progressTo = max;
            playerLvl++;
        }
        levelProgressBar.setProgress(startExp);
        levelProgressBar.setMax(max);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(levelProgressBar, "progress", startExp, progressTo);
        progressAnimator.setDuration(2000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        if (lvlUp) {
            final int finalPlayerLvl = playerLvl;
            progressAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    displayExp(0, increase - max + startExp, finalPlayerLvl);
                }
            });
        }
        progressAnimator.start();
    }

    private Result isPlayerAWinner(Battle battle, User player) {
        if (battle.getWinner().getUsername().equals(player.getUsername())) return Result.WIN;
        else if (battle.getLoser().getUsername().equals(player.getUsername())) return Result.LOSE;
        else return Result.DRAW;

    }

    @OnClick(R.id.dialog_confirm_btn)
    public void onDialogConfirmClick() {
        dismiss();
    }
}
