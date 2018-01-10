package com.jakub.rockpaperscissorswars.models;

import com.jakub.rockpaperscissorswars.config.ConfigController;
import com.jakub.rockpaperscissorswars.constants.AppConstants;

import org.parceler.Parcel;

/**
 * Created by Emil on 2018-01-06.
 */
@Parcel(Parcel.Serialization.BEAN)
public class User {
    private String email;
    private String username;
    private int lvl;
    private int experience;
    private int experienceToNextLvl;
    private int skillPoints;
    private int rockVal;
    private int paperVal;
    private int scissorsVal;
    private int health;
    private int defence;
    private int victories;

    //Pusty konstruktor dla parcelera
    public User() {
    }

    //Kompletny konstruktor
    public User(String email, String username, int lvl, int experience, int rockVal, int paperVal, int scissorsVal, int health, int defence, int victories) {
        this.email = email;
        this.username = username;
        this.lvl = lvl;
        this.experience = experience;
        this.rockVal = rockVal;
        this.paperVal = paperVal;
        this.scissorsVal = scissorsVal;
        this.health = health;
        this.defence = defence;
        this.victories = victories;
    }

    //Dla nowych użytkowników
    public User(String email, String username, int rockVal, int paperVal, int scissorsVal) {
        this.email = email;
        this.username = username;
        this.lvl = 1;
        this.experience = 0;
        this.rockVal = rockVal;
        this.paperVal = paperVal;
        this.scissorsVal = scissorsVal;
        this.health = ConfigController.getConfig().getDefaultHealth();
        this.defence = ConfigController.getConfig().getDefaultDefence();
        this.victories = 0;
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

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getExperienceToNextLvl() {
        return experienceToNextLvl;
    }

    public void setExperienceToNextLvl(int experienceToNextLvl) {
        this.experienceToNextLvl = experienceToNextLvl;
    }
}
