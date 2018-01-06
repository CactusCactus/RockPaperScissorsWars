package com.jakub.rockpaperscissorswars;

/**
 * Created by Emil on 2018-01-06.
 */

public class User {
    private String email;
    private String username;
    private int experience;
    private int rockVal;
    private int paperVal;
    private int scissorsVal;
    private int health;
    private int defence;
    private int victories;

    public User() {
    }

    public User(String email, String username, int experience, int rockVal, int paperVal, int scissorsVal, int health, int defence, int victories) {
        this.email = email;
        this.username = username;
        this.experience = experience;
        this.rockVal = rockVal;
        this.paperVal = paperVal;
        this.scissorsVal = scissorsVal;
        this.health = health;
        this.defence = defence;
        this.victories = victories;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getRockVal() {
        return rockVal;
    }

    public void setRockVal(int rockVal) {
        this.rockVal = rockVal;
    }

    public int getPaperVal() {
        return paperVal;
    }

    public void setPaperVal(int paperVal) {
        this.paperVal = paperVal;
    }

    public int getScissorsVal() {
        return scissorsVal;
    }

    public void setScissorsVal(int scissorsVal) {
        this.scissorsVal = scissorsVal;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }
}
