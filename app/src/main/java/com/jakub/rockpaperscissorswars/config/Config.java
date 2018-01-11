package com.jakub.rockpaperscissorswars.config;

import com.google.firebase.database.PropertyName;

/**
 * Created by Emil on 2018-01-10.
 */

public class Config {
    private int baseStat;
    private int chosenWeaponBonus;
    private int defaultHealth;
    private int defaultDefence;
    private int healthIncrease;
    private int skillIncrease;
    private int defenceIncrease;
    private int expToLvlRatio;
    private int lvlUpPower;
    private int skillPointsOnLvlUp;

    @PropertyName("base_stat")
    public int getBaseStat() {
        return baseStat;
    }
    @PropertyName("base_stat")
    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }
    @PropertyName("chosen_weapon_bonus")
    public int getChosenWeaponBonus() {
        return chosenWeaponBonus;
    }
    @PropertyName("chosen_weapon_bonus")
    public void setChosenWeaponBonus(int chosenWeaponBonus) {
        this.chosenWeaponBonus = chosenWeaponBonus;
    }
    @PropertyName("default_health")
    public int getDefaultHealth() {
        return defaultHealth;
    }
    @PropertyName("default_health")
    public void setDefaultHealth(int defaultHealth) {
        this.defaultHealth = defaultHealth;
    }
    @PropertyName("default_defence")
    public int getDefaultDefence() {
        return defaultDefence;
    }
    @PropertyName("default_defence")
    public void setDefaultDefence(int defaultDefence) {
        this.defaultDefence = defaultDefence;
    }
    @PropertyName("health_increase")
    public int getHealthIncrease() {
        return healthIncrease;
    }
    @PropertyName("health_increase")
    public void setHealthIncrease(int healthIncrease) {
        this.healthIncrease = healthIncrease;
    }
    @PropertyName("skill_increase")
    public int getSkillIncrease() {
        return skillIncrease;
    }
    @PropertyName("skill_increase")
    public void setSkillIncrease(int skillIncrease) {
        this.skillIncrease = skillIncrease;
    }
    @PropertyName("defence_increase")
    public int getDefenceIncrease() {
        return defenceIncrease;
    }
    @PropertyName("defence_increase")
    public void setDefenceIncrease(int defenceIncrease) {
        this.defenceIncrease = defenceIncrease;
    }

    @PropertyName("exp_to_lvl_ratio")
    public int getExpToLvlRatio() {
        return expToLvlRatio;
    }
    @PropertyName("exp_to_lvl_ratio")
    public void setExpToLvlRation(int expToLvlRation) {
        this.expToLvlRatio = expToLvlRation;
    }
    @PropertyName("lvl_up_power")
    public int getLvlUpPower() {
        return lvlUpPower;
    }
    @PropertyName("lvl_up_power")
    public void setLvlUpPower(int lvlUpPower) {
        this.lvlUpPower = lvlUpPower;
    }

    @PropertyName("skill_points_on_lvl_up")
    public int getSkillPointsOnLvlUp() {
        return skillPointsOnLvlUp;
    }
    @PropertyName("skill_points_on_lvl_up")
    public void setSkillPointsOnLvlUp(int skillPointsOnLvlUp) {
        this.skillPointsOnLvlUp = skillPointsOnLvlUp;
    }
}
