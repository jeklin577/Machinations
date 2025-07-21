package com.machinations.game;

import java.util.HashMap;
import java.util.Map;

public class Skills {
    public void incrementSkill(Skill skill) {
        skillValues.put(skill, getSkillValue(skill) + 1);
    }

    public enum Skill {
        // Everyman Skills
        CLIMB(SkillType.EVERYMAN),
        LANGUAGES(SkillType.EVERYMAN),
        SEARCH(SkillType.EVERYMAN),
        SECURITY(SkillType.EVERYMAN),
        SLEIGHT_OF_HAND(SkillType.EVERYMAN),
        SNEAK_ATTACK(SkillType.EVERYMAN),
        STEALTH(SkillType.EVERYMAN),
        STRUCTURE(SkillType.EVERYMAN),
        SURVIVAL(SkillType.EVERYMAN),
        TINKER(SkillType.EVERYMAN),

        // Psion Skills
        DISTANT_MIND(SkillType.PSION),
        INTUITION(SkillType.PSION),
        MENTAL_ARMOUR(SkillType.PSION),
        PENETRATING_INSIGHT(SkillType.PSION),
        POWER_RESERVE(SkillType.PSION),
        PSYCHIC_TRAINING(SkillType.PSION),
        RAVAGING_INTELLECT(SkillType.PSION),

        /// Combat Skills
        AMBUSH(SkillType.COMBAT),
        ARMOUR_EATER(SkillType.COMBAT),
        BLEEDING_CUT(SkillType.COMBAT),
        CHINK(SkillType.COMBAT),
        COMBAT_DODGE(SkillType.COMBAT),
        COMBAT_REFLEXES(SkillType.COMBAT),
        CRIPPLE_ATTACK(SkillType.COMBAT),
        CRIPPLE_DEFENCE(SkillType.COMBAT),
        CRIPPLE_MOVEMENT(SkillType.COMBAT),
        DEADLY_SHOT(SkillType.COMBAT),
        DEFENSIVE_GUNFIGHTER(SkillType.COMBAT),
        DOUBLE_WEAPON(SkillType.COMBAT),
        FLURRY_OF_BLOWS(SkillType.COMBAT),
        HAIL_OF_BULLETS(SkillType.COMBAT),
        HOLD(SkillType.COMBAT),
        JUGGERNAUT(SkillType.COMBAT),
        KNOCK_OUT_BLOW(SkillType.COMBAT),
        NECK_HAIRS(SkillType.COMBAT),
        POWER_ATTACK(SkillType.COMBAT),
        READY_BLOW(SkillType.COMBAT),
        SHORT_CONTROLLED_BURSTS(SkillType.COMBAT),
        SNIPE(SkillType.COMBAT),
        STUN_ATTACK(SkillType.COMBAT),
        TACTICAL_COMMAND(SkillType.COMBAT),
        TRIP_ATTACK(SkillType.COMBAT),
        WEAPON_EXPERT(SkillType.COMBAT),
        WRESTLE(SkillType.COMBAT),



        // Scholar Skills
        EXOTECH(SkillType.SCHOLAR),
        EXPERIMENTAL_TECH(SkillType.SCHOLAR),
        HACKER(SkillType.SCHOLAR),
        HUMAN_COMPUTER(SkillType.SCHOLAR),
        MEDICINE(SkillType.SCHOLAR),
        REPURPOSE(SkillType.SCHOLAR),
        ROBOTIC_COMPANION(SkillType.SCHOLAR),
        SUPERCHARGE(SkillType.SCHOLAR),
        TRAINED_ANIMAL(SkillType.SCHOLAR),
        XENOARCHAEOLOGY(SkillType.SCHOLAR),
        XENOPSYCH(SkillType.SCHOLAR),

        // General Skills
        DRIVE(SkillType.GENERAL),
        FIRST_AID(SkillType.GENERAL),
        LORE(SkillType.GENERAL),                  // Specific
        LOVER(SkillType.GENERAL),
        MAKE(SkillType.GENERAL),                  // Specific
        PERFORM(SkillType.GENERAL),               // Specific
        PILOT_STARSHIP(SkillType.GENERAL),
        RIDING(SkillType.GENERAL),
        SAIL(SkillType.GENERAL),
        SAVINGS(SkillType.GENERAL),
        TRAINING_SAVING_ROLL(SkillType.GENERAL),  // Specific
        TRAINING_ATTRIBUTE(SkillType.GENERAL),    // Specific
        WEB_OF_CONTACTS(SkillType.GENERAL),
        INVESTMENT(SkillType.GENERAL);


        private final SkillType type;

        Skill(SkillType type) {
            this.type = type;
        }

        public SkillType getType() {
            return type;
        }
    }

    public enum SkillType {
        EVERYMAN,
        SCHOLAR,
        PSION,
        COMBAT,
        GENERAL,
    }

    ///Note, we should change this so skills are properly seregated into the everyman, scholar, psion sets

    private final Map<Skill, Integer> skillValues = new HashMap<>();

    public void setSkillValue(Skill skill, int value) {
        skillValues.put(skill, value);
    }

    public int getSkillValue(Skill skill) {
        return skillValues.getOrDefault(skill, 0);
    }


    public boolean performSkillCheck(PlayerCharacter player, Skill skill) {
        int skillValue = getSkillValue(skill);
        int diceRoll = Dice.rollDice(1, 6);  // Use the Dice class to roll a d6

        System.out.println("Performing skill check for skill: " + skill);
        System.out.println("Player's skill value: " + skillValue);
        System.out.println("Dice roll result: " + diceRoll);
        ///Note, these are prototype bits, comment these out when we actually use these in the overworld.

        if (diceRoll < skillValue) {
            System.out.println("Skill check passed!");
            return true;
        } else {
            System.out.println("Skill check failed!");
            return false;
        }
        ///Should also edit this so that each skillcheck has a pass and fail event, that triggers specifics (Ie, damage, or changing a tile to be navigable or something
    }
}
