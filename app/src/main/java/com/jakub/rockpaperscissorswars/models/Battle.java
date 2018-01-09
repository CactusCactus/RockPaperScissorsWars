package com.jakub.rockpaperscissorswars.models;

import com.google.firebase.database.PropertyName;
import com.jakub.rockpaperscissorswars.constants.AttackType;

import org.parceler.Parcel;

/**
 * Created by Emil on 2018-01-07.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Battle {
    @PropertyName("firstPlayer")
    private User firstPlayer;
    @PropertyName("secondPlayer")
    private User secondPlayer;
    private int firstPlayerHp;
    private int secondPlayerHp;
    private AttackType firstPlayerMove;
    private AttackType secondPlayerMove;
    @PropertyName("winner")
    private User winner;
    @PropertyName("loser")
    private User loser;

    public Battle() {
    }

    public Battle(User firstPlayer, User secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerHp = firstPlayer.getHealth();
        this.secondPlayerHp = secondPlayer != null ? secondPlayer.getHealth() : 0;
    }
    @PropertyName("firstPlayer")
    public User getFirstPlayer() {
        return firstPlayer;
    }
    @PropertyName("firstPlayer")
    public void setFirstPlayer(User firstPlayer) {
        this.firstPlayer = firstPlayer;
        this.firstPlayerHp = firstPlayer.getHealth();
    }
    @PropertyName("secondPlayer")
    public User getSecondPlayer() {
        return secondPlayer;
    }
    @PropertyName("secondPlayer")
    public void setSecondPlayer(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        this.secondPlayerHp = secondPlayer.getHealth();
    }
    @PropertyName("firstPlayerHp")
    public int getFirstPlayerHp() {
        return firstPlayerHp;
    }
    @PropertyName("firstPlayerHp")
    public void setFirstPlayerHp(int firstPlayerHp) {
        this.firstPlayerHp = firstPlayerHp;
    }
    @PropertyName("secondPlayerHp")
    public int getSecondPlayerHp() {
        return secondPlayerHp;
    }
    @PropertyName("secondPlayerHp")
    public void setSecondPlayerHp(int secondPlayerHp) {
        this.secondPlayerHp = secondPlayerHp;
    }
    @PropertyName("firstPlayerMove")
    public AttackType getFirstPlayerMove() {
        return firstPlayerMove;
    }
    @PropertyName("firstPlayerMove")
    public void setFirstPlayerMove(AttackType firstPlayerMove) {
        this.firstPlayerMove = firstPlayerMove;
    }
    @PropertyName("secondPlayerMove")
    public AttackType getSecondPlayerMove() {
        return secondPlayerMove;
    }
    @PropertyName("secondPlayerMove")
    public void setSecondPlayerMove(AttackType secondPlayerMove) {
        this.secondPlayerMove = secondPlayerMove;
    }
    @PropertyName("winner")
    public User getWinner() {
        return winner;
    }
    @PropertyName("winner")
    public void setWinner(User winner) {
        this.winner = winner;
    }
    @PropertyName("loser")
    public User getLoser() {
        return loser;
    }
    @PropertyName("loser")
    public void setLoser(User loser) {
        this.loser = loser;
    }
}
