package com.jakub.rockpaperscissorswars.models;

import com.jakub.rockpaperscissorswars.constants.AttackType;

import org.parceler.Parcel;

/**
 * Created by Emil on 2018-01-07.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Battle {
    private User firstPlayer;
    private User secondPlayer;
    private int firstPlayerHp;
    private int secondPlayerHp;
    private AttackType firstPlayerMove;
    private AttackType secondPlayerMove;
    private User winner;
    private User loser;
    private boolean firstPlayerFinished;  //TODO a może nie xd
    private boolean secondPlayerFinished; //TODO

    public Battle() {
    }

    public Battle(User firstPlayer, User secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerHp = firstPlayer.getHealth();
        this.secondPlayerHp = secondPlayer != null ? secondPlayer.getHealth() : 0;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(User firstPlayer) {
        this.firstPlayer = firstPlayer;
        this.firstPlayerHp = firstPlayer.getHealth();
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        this.secondPlayerHp = secondPlayer.getHealth();
    }

    public int getFirstPlayerHp() {
        return firstPlayerHp;
    }

    public void setFirstPlayerHp(int firstPlayerHp) {
        this.firstPlayerHp = firstPlayerHp;
    }

    public int getSecondPlayerHp() {
        return secondPlayerHp;
    }

    public void setSecondPlayerHp(int secondPlayerHp) {
        this.secondPlayerHp = secondPlayerHp;
    }

    public AttackType getFirstPlayerMove() {
        return firstPlayerMove;
    }

    public void setFirstPlayerMove(AttackType firstPlayerMove) {
        this.firstPlayerMove = firstPlayerMove;
    }

    public AttackType getSecondPlayerMove() {
        return secondPlayerMove;
    }

    public void setSecondPlayerMove(AttackType secondPlayerMove) {
        this.secondPlayerMove = secondPlayerMove;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public boolean isFirstPlayerFinished() {
        return firstPlayerFinished;
    }

    public void setFirstPlayerFinished(boolean firstPlayerFinished) {
        this.firstPlayerFinished = firstPlayerFinished;
    }

    public boolean isSecondPlayerFinished() {
        return secondPlayerFinished;
    }

    public void setSecondPlayerFinished(boolean secondPlayerFinished) {
        this.secondPlayerFinished = secondPlayerFinished;
    }
}
