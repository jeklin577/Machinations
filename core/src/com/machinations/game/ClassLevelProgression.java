package com.machinations.game;

import java.util.HashMap;
import java.util.Map;

public class ClassLevelProgression {
    private Map<String, Map<Integer, LevelProgression>> classProgressions = new HashMap<>();

    private void addClassProgression(String className, Map<Integer, LevelProgression> levelProgressions) {
        classProgressions.put(className, levelProgressions);
    }

    public LevelProgression getProgressionForClassAndLevel(String className, int level) {
        return classProgressions.getOrDefault(className, new HashMap<>()).get(level);
    }

    public void initializeClassProgressions() {

        // Expert
        addClassProgression("Expert", createLevelProgressions(
                new int[]{0, 1500, 3000, 6000, 12000, 24000, 48000, 96000, 192000, 288000,
                        384000, 480000, 576000, 672000, 768000, 864000, 960000, 1056000, 1152000, 1248000},
                new int[]{4, 3, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                1, 6)); // health dice: 1d6

        // Killer
        addClassProgression("Killer", createLevelProgressions(
                new int[]{0, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000, 384000,
                        512000, 640000, 768000, 896000, 1024000, 1152000, 1280000, 1408000, 1536000, 1664000},
                new int[]{2, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                1, 8)); // health dice: 1d8

        // Scholar
        addClassProgression("Scholar", createLevelProgressions(
                new int[]{0, 2700, 5400, 9000, 13500, 18900, 25200, 32400, 40500, 49500,
                        59400, 70200, 81900, 94500, 108000, 122400, 137700, 153900, 171000, 189000},
                new int[]{4, 3, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                1, 6)); // health dice: 1d6

        // Psion
        addClassProgression("Psion", createLevelProgressions(
                new int[]{0, 3600, 7200, 12000, 18000, 25200, 33600, 43200, 54000, 66000,
                        79200, 93600, 109200, 126000, 144000, 163200, 183600, 205200, 228000, 252000},
                new int[]{4, 3, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                1, 6)); // health dice: 1d6

    }

    public Integer getSkillPointsForClassAndLevel(String className, int level) {
        LevelProgression progression = getProgressionForClassAndLevel(className, level);
        if (progression != null) {
            return progression.getSkillPoints(); // assumes LevelProgression has a getter
        }
        return null; // or 0 if you prefer a default
    }



    // Overloaded method to handle array inputs for XP and skill points
    private Map<Integer, LevelProgression> createLevelProgressions(int[] experiences, int[] skillPoints, int healthDiceRolls, int healthDiceSize) {
        Map<Integer, LevelProgression> levelProgressions = new HashMap<>();
        for (int i = 0; i < experiences.length; i++) {
            int level = i + 1;
            int xp = experiences[i];
            int sp = skillPoints[i];
            LevelProgression progression = new LevelProgression(healthDiceRolls, healthDiceSize, xp, sp);
            levelProgressions.put(level, progression);
        }
        return levelProgressions;
    }
}
