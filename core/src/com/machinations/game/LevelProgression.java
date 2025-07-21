package com.machinations.game;



public class LevelProgression {
    private int healthDiceRolls;
    private int healthDiceSize;
    private int experienceForNextLevel;

    public LevelProgression(int healthDiceRolls, int healthDiceSize, int experienceForNextLevel) {
        this.healthDiceRolls = healthDiceRolls;
        this.healthDiceSize = healthDiceSize;
        this.experienceForNextLevel = experienceForNextLevel;
    }

    public int getHealthDiceRolls() {
        return healthDiceRolls;
    }

    public int getHealthDiceSize() {
        return healthDiceSize;
    }

    public int getExperienceForNextLevel() {
        return experienceForNextLevel;
    }
}

