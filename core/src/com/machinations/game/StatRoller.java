package com.machinations.game;


import java.util.*;



public class StatRoller {
    private static final List<String> ABILITIES = Arrays.asList(
            "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"
    );

    public static List<Integer> rollStats(StatRollMode mode) {
        List<Integer> stats = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            if (mode == StatRollMode.PUSSY || mode == StatRollMode.EASY) {
                stats.add(roll4d6DropLowest());
            } else {
                stats.add(Dice.rollDice(3, 6));
            }
        }

        return stats;
    }

    public static Map<String, Integer> assignStats(List<Integer> rolledStats, StatRollMode mode) {
        Map<String, Integer> assigned = new LinkedHashMap<>();

        if (mode == StatRollMode.EASY || mode == StatRollMode.HARDCORE) {
            for (int i = 0; i < ABILITIES.size(); i++) {
                assigned.put(ABILITIES.get(i), rolledStats.get(i));
            }
        } else {
            for (String ability : ABILITIES) {
                assigned.put(ability, 0); // placeholder — assign later
            }
        }

        return assigned;
    }

    private static int roll4d6DropLowest() {
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rolls.add(Dice.rollDice(1, 6));
        }
        Collections.sort(rolls);
        return rolls.get(1) + rolls.get(2) + rolls.get(3); // drop lowest
    }
}