package com.machinations.game;



public class LevelProgression {
    private final int healthDiceRolls;
    private final int healthDiceSize;
    private final int experienceForNextLevel;
    private final int skillPoints; // NEW

    public LevelProgression(int healthDiceRolls, int healthDiceSize, int experienceForNextLevel, int skillPoints) {
        this.healthDiceRolls = healthDiceRolls;
        this.healthDiceSize = healthDiceSize;
        this.experienceForNextLevel = experienceForNextLevel;
        this.skillPoints = skillPoints;
    }

    public int getHealthDiceRolls() { return healthDiceRolls; }
    public int getHealthDiceSize() { return healthDiceSize; }
    public int getExperienceForNextLevel() { return experienceForNextLevel; }
    public int getSkillPoints() { return skillPoints; } // NEW
}
