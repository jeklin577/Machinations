package com.machinations.game;

import java.util.HashMap;
import java.util.Map;

public class ClassLevelProgression {
    private Map<String, Map<Integer, LevelProgression>> classProgressions = new HashMap<>();

    public void addClassProgression(String className, int level, LevelProgression progression) {
        classProgressions.computeIfAbsent(className, k -> new HashMap<>()).put(level, progression);
    }

    public LevelProgression getProgressionForClassAndLevel(String className, int level) {
        return classProgressions.getOrDefault(className, new HashMap<>()).get(level);
    }

    public void initializeClassProgressions() {

        addClassProgression("Expert", createLevelProgressions(
                1000, 3000, 6000, 10000, 15000, 21000, 28000, 36000, 45000, 55000));

        addClassProgression("Killer", createLevelProgressions(
                800, 2400, 4800, 8000, 12000, 16800, 22400, 28800, 36000, 44000));

        addClassProgression("Scholar", createLevelProgressions(
                900, 2700, 5400, 9000, 13500, 18900, 25200, 32400, 40500, 49500));

        addClassProgression("Psion", createLevelProgressions(
                1200, 3600, 7200, 12000, 18000, 25200, 33600, 43200, 54000, 66000));


    }

    private void addClassProgression(String className, Map<Integer, LevelProgression> levelProgressions) {
        for (Map.Entry<Integer, LevelProgression> entry : levelProgressions.entrySet()) {
            addClassProgression(className, entry.getKey(), entry.getValue());
        }
    }

    private Map<Integer, LevelProgression> createLevelProgressions(int... experiences) {
        Map<Integer, LevelProgression> levelProgressions = new HashMap<>();
        for (int i = 0; i < experiences.length; i++) {
            int level = i + 1;
            int experienceForNextLevel = experiences[i];
            int healthDiceRolls = 1;
            int healthDiceSize = 10;
            LevelProgression progression = new LevelProgression(healthDiceRolls, healthDiceSize, experienceForNextLevel);
            levelProgressions.put(level, progression);
        }
        return levelProgressions;
    }
}
