package com.machinations.game;

import java.util.ArrayList;
import java.util.List;

public class Party {
    private static final int MAX_PARTY_SIZE = 4;
    private List<PlayerCharacter> members;

    PlayerCharacter best = null;
    int highestValue = -1;

    public Party() {
        members = new ArrayList<>();
    }

    public boolean addMember(PlayerCharacter character) {
        if (members.size() < MAX_PARTY_SIZE) {
            return members.add(character);
        }
        return false; // Party full
    }

    public boolean removeMember(PlayerCharacter character) {
        return members.remove(character);
    }

    public List<PlayerCharacter> getMembers() {
        return members;
    }

    public PlayerCharacter getLeader() {
        return members.isEmpty() ? null : members.get(0);
    }

    public int getSize() {
        return members.size();
    }

    public boolean isFull() {
        return members.size() >= MAX_PARTY_SIZE;
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public boolean hasSkill(Skills.Skill skill) {
        for (PlayerCharacter member : members) {
            if (member.getSkills().getSkillValue(skill) > 0) {
                return true;
            }
        }
        return false;
    }


        public PlayerCharacter getBestCharacterForSkill(Skills.Skill skill) {
            PlayerCharacter best = null;
            int highest = -1;

            for (PlayerCharacter pc : members) {
                int value = pc.getSkills().getSkillValue(skill);
                if (value > highest && value > 0) { // Only consider PCs with actual skill ranks
                    highest = value;
                    best = pc;
                }
            }
            return best;
        }


    public boolean performGroupSkillCheck(Skills.Skill skill) {
        PlayerCharacter best = getBestCharacterForSkill(skill);
        if (best == null) {
            System.out.println("No characters in the party have the skill: " + skill);
            return false;
        }

        boolean result = best.getSkills().performSkillCheck(best, skill);
        if (result) {
            System.out.println(best.getName() + " pulled it off!");
        } else {
            System.out.println(best.getName() + " failed the attempt.");
        }
        return result;
    }


}