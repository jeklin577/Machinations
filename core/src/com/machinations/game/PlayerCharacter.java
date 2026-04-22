package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.machinations.game.Dice.rollDice;

public class PlayerCharacter {
    // Basic information
    private String name;
    private String species;
    private String classType;
    private int level;
    private int experiencePoints;
    private int health;

    private Texture portrait;

    private int chaScore;
    private int chaMod;
    private int comScore;
    private int comMod;
    private int conScore;
    private int conMod;
    private int dexScore;
    private int dexMod;
    private int strScore;
    private int strMod;
    private int intScore;
    private int intMod;
    private int wisScore;
    private int wisMod;
    private int MaxHP;

    private int unspentSkillPoints;
    private int attackBonus;

    private boolean canFly;
    private boolean canFitThroughSmallSpaces;
    private int speed;
    private int swimspeed;

    private int reflexSave;
    private int charmSave;
    private int toughnessSave;
    private int logicSave;
    private int willSave;
    private int powerSave;
    private int looksSave;

    private int rangedDefence;
    private int closeDefence;

    private String armorType;
    private int armorDamageNegation;
    private int armorDefensePenalty;

    private int rangedDamageBonus;
    private int closeDamageBonus;

    private int currentHp;  // Variable to track current health
    private Dice rangedDamageDice; // Variable for ranged damage dice
    private Dice closeDamageDice;  // Variable for close damage dice
    private Dice ArmourDie;
    private RaceTrait raceTrait;

    private Set<DamageType> resistances = new HashSet<>();
    private Set<DamageType> vulnerabilities = new HashSet<>();

    private ClassLevelProgression classLevelProgression;
    private Skills skills;

    private int baseStrScore;
    private int baseDexScore;
    private int baseConScore;
    private int baseIntScore;
    private int baseWisScore;
    private int baseChaScore;
    private int baseComScore;

    private int baseUnspentSkillPoints;
    private Skills baseSkills;
    private ArrayList<RaceTrait> raceTraits;


    public PlayerCharacter(String name, String species, String classType, ClassLevelProgression classLevelProgression,
                           int chaScore, int comScore, int conScore, int dexScore, int strScore, int intScore, int wisScore) {
        this.name = name;
        this.species = species;
        this.classType = classType;
        this.level = 1;
        this.experiencePoints = 0;
        this.health = getInitialHealth(classType);
        this.classLevelProgression = classLevelProgression;
        this.intScore = intScore;
        this.intMod = getModifier(intScore);
        this.conScore = conScore;
        this.conMod = getModifier(conScore);
        this.wisScore = wisScore;
        this.wisMod = getModifier(wisScore);
        this.strScore = strScore;
        this.strMod = getModifier(strScore);
        this.dexScore = dexScore;
        this.dexMod = getModifier(dexScore);
        this.comScore = comScore;
        this.comMod = getModifier(comScore);
        this.chaScore = chaScore;
        this.chaMod = getModifier(chaScore);
        this.attackBonus = 0;
        this.canFly = false;
        this.reflexSave = dexScore / 2;
        this.charmSave = chaScore / 2;
        this.toughnessSave = conScore / 2;
        this.logicSave = intScore / 2;
        this.willSave = wisScore / 2;
        this.powerSave = strScore / 2;
        this.looksSave = comScore / 2;
        this.rangedDefence = 12 + this.dexMod;
        this.closeDefence = 12 + this.conMod;
        this.rangedDamageBonus = this.attackBonus + this.dexMod;
        this.closeDamageBonus = this.attackBonus + this.strMod;
        this.attackBonus = 0;
        this.swimspeed = this.speed/2;


        this.currentHp = this.health;  // Initialize currentHp to be equal to health
        this.MaxHP = currentHp; //Sets initial max HP to the health, in line with motsp, so if you start with a d4 health die, you get 4 hp.



        this.skills = new Skills();
        initializeSkills();
        Texture DefaultPortrait = new Texture(Gdx.files.internal("Portraits/RebutPortrait.png"));
        this.portrait = DefaultPortrait;
    }

    private void initializeSkills() {
        switch (classType) {
            case "Psion":
                initializePsionSkills();
                break;
            case "Killer":
            case "Expert":
                initializeKillerExpertSkills();
                break;
            case "Scholar":
                initializeScholarSkills();
                break;
            default:
                initializeGeneralSkills();
                break;
        }
    }

    public void resetToBaseState() {
        this.strScore = baseStrScore;
        this.dexScore = baseDexScore;
        this.conScore = baseConScore;
        this.intScore = baseIntScore;
        this.wisScore = baseWisScore;
        this.chaScore = baseChaScore;
        this.comScore = baseComScore;

        this.strMod = getModifier(strScore);
        this.dexMod = getModifier(dexScore);
        this.conMod = getModifier(conScore);
        this.intMod = getModifier(intScore);
        this.wisMod = getModifier(wisScore);
        this.chaMod = getModifier(chaScore);
        this.comMod = getModifier(comScore);

        this.reflexSave = dexScore / 2;
        this.charmSave = chaScore / 2;
        this.toughnessSave = conScore / 2;
        this.logicSave = intScore / 2;
        this.willSave = wisScore / 2;
        this.powerSave = strScore / 2;
        this.looksSave = comScore / 2;

        this.rangedDefence = 12 + dexMod;
        this.closeDefence = 12 + conMod;
        this.rangedDamageBonus = attackBonus + dexMod;
        this.closeDamageBonus = attackBonus + strMod;

        this.unspentSkillPoints = baseUnspentSkillPoints;
        this.skills = baseSkills.copy(); // make a real copy method

        this.resistances.clear();
        this.vulnerabilities.clear();

        this.canFly = false;
        this.canFitThroughSmallSpaces = false;
        this.speed = 10;
        this.swimspeed = this.speed / 2;
    }

    private void initializeGeneralSkills() {
        // General Skills
        skills.setSkillValue(Skills.Skill.CLIMB, 0);
        skills.setSkillValue(Skills.Skill.LANGUAGES, 0);
        skills.setSkillValue(Skills.Skill.SEARCH, 0);
        skills.setSkillValue(Skills.Skill.SECURITY, 0);
        skills.setSkillValue(Skills.Skill.SLEIGHT_OF_HAND, 0);
        skills.setSkillValue(Skills.Skill.SNEAK_ATTACK, 0);
        skills.setSkillValue(Skills.Skill.STEALTH, 0);
        skills.setSkillValue(Skills.Skill.STRUCTURE, 0);
        skills.setSkillValue(Skills.Skill.SURVIVAL, 0);
        skills.setSkillValue(Skills.Skill.TINKER, 0);
        skills.setSkillValue(Skills.Skill.INVESTMENT, 0);
    }

    private void initializePsionSkills() {
        initializeGeneralSkills();
        // Psion Skills
        skills.setSkillValue(Skills.Skill.DISTANT_MIND, 0);
        skills.setSkillValue(Skills.Skill.INTUITION, 0);
        skills.setSkillValue(Skills.Skill.MENTAL_ARMOUR, 0);
        skills.setSkillValue(Skills.Skill.PENETRATING_INSIGHT, 0);
        skills.setSkillValue(Skills.Skill.POWER_RESERVE, 0);
        skills.setSkillValue(Skills.Skill.PSYCHIC_TRAINING, 0);
        skills.setSkillValue(Skills.Skill.RAVAGING_INTELLECT, 0);
    }

    private void initializeKillerExpertSkills() {
        initializeGeneralSkills();
        // Killer/Expert Skills
        skills.setSkillValue(Skills.Skill.AMBUSH, 0);
        skills.setSkillValue(Skills.Skill.ARMOUR_EATER, 0);
        skills.setSkillValue(Skills.Skill.BLEEDING_CUT, 0);
        skills.setSkillValue(Skills.Skill.CHINK, 0);
        skills.setSkillValue(Skills.Skill.COMBAT_DODGE, 0);
        skills.setSkillValue(Skills.Skill.COMBAT_REFLEXES, 0);
        skills.setSkillValue(Skills.Skill.CRIPPLE_ATTACK, 0);
        skills.setSkillValue(Skills.Skill.CRIPPLE_DEFENCE, 0);
        skills.setSkillValue(Skills.Skill.CRIPPLE_MOVEMENT, 0);
        skills.setSkillValue(Skills.Skill.DEADLY_SHOT, 0);
        skills.setSkillValue(Skills.Skill.DEFENSIVE_GUNFIGHTER, 0);
        skills.setSkillValue(Skills.Skill.DOUBLE_WEAPON, 0);
        skills.setSkillValue(Skills.Skill.FLURRY_OF_BLOWS, 0);
        skills.setSkillValue(Skills.Skill.HAIL_OF_BULLETS, 0);
        skills.setSkillValue(Skills.Skill.HOLD, 0);
        skills.setSkillValue(Skills.Skill.JUGGERNAUT, 0);
        skills.setSkillValue(Skills.Skill.KNOCK_OUT_BLOW, 0);
        skills.setSkillValue(Skills.Skill.NECK_HAIRS, 0);
        skills.setSkillValue(Skills.Skill.POWER_ATTACK, 0);
        skills.setSkillValue(Skills.Skill.READY_BLOW, 0);
        skills.setSkillValue(Skills.Skill.SHORT_CONTROLLED_BURSTS, 0);
        skills.setSkillValue(Skills.Skill.SNIPE, 0);
        skills.setSkillValue(Skills.Skill.STUN_ATTACK, 0);
        skills.setSkillValue(Skills.Skill.TACTICAL_COMMAND, 0);
        skills.setSkillValue(Skills.Skill.TRIP_ATTACK, 0);
        skills.setSkillValue(Skills.Skill.WEAPON_EXPERT, 0);
        skills.setSkillValue(Skills.Skill.WRESTLE, 0);
    }

    private void initializeScholarSkills() {
        initializeGeneralSkills();
        // Scholar Skills
        skills.setSkillValue(Skills.Skill.EXOTECH, 0);
        skills.setSkillValue(Skills.Skill.EXPERIMENTAL_TECH, 0);
        skills.setSkillValue(Skills.Skill.HACKER, 0);
        skills.setSkillValue(Skills.Skill.HUMAN_COMPUTER, 0);
        skills.setSkillValue(Skills.Skill.MEDICINE, 0);
        skills.setSkillValue(Skills.Skill.REPURPOSE, 0);
        skills.setSkillValue(Skills.Skill.ROBOTIC_COMPANION, 0);
        skills.setSkillValue(Skills.Skill.SUPERCHARGE, 0);
        skills.setSkillValue(Skills.Skill.TRAINED_ANIMAL, 0);
        skills.setSkillValue(Skills.Skill.XENOARCHAEOLOGY, 0);
        skills.setSkillValue(Skills.Skill.XENOPSYCH, 0);
    }

    public void setRaceTraits(ArrayList<RaceTrait> traits) {
        this.raceTraits = traits;
    }

    // Method to gain experience points and check for level up
    public void addExperience(int amount) {
        experiencePoints += amount;
        LevelProgression progression = classLevelProgression.getProgressionForClassAndLevel(classType, level);
        if (experiencePoints >= progression.getExperienceForNextLevel()) {
            experiencePoints -= progression.getExperienceForNextLevel();
            levelUp();
        }
    }

    // Method to check if the player should level up


    // Method to handle leveling up
    private void levelUp() {
        level++;
        calculateHealthIncrease();
    }

    private void calculateHealthIncrease() {
        LevelProgression progression = classLevelProgression.getProgressionForClassAndLevel(classType, level);
        int healthIncrease = rollDice(progression.getHealthDiceRolls(), progression.getHealthDiceSize()) + conMod;
        health += healthIncrease;
    }


    // Method to get the initial health based on the class
    private int getInitialHealth(String classType) {
        switch (classType) {
            case "Expert":
                return 8;
            case "Killer":
                 return 6;
            case "Scholar":
                 return 6;
            case "Psion":
                return 4;
            default:
                return new Dice(6).roll(); ///This was what
        }
    }

    // Method to get the base health increase based on the class
    private int getBaseHealthIncrease(String classType) {
        switch (classType) {
            case "Expert":
                return 5;
            case "Killer":
                return 4;
            case "Scholar":
                return 3;
            case "Psion":
                return 2;
            default:
                return 3;
        }
    }

    // Method to get the number of sides of the health dice based on the class
    private int getHealthDiceSides(String classType) {
        switch (classType) {
            case "Expert":
                return 8;
            case "Killer":
                return 6;
            case "Scholar":
                return 6;
            case "Psion":
                return 4;
            default:
                return 6;
        }
    }

    int getModifier(int stat) {
            return (stat - 10) / 2;
    }


    // Getter and setter methods for all scores and modifiers
    public int getChaScore() {
        return chaScore;
    }

    public String getName() {
        return this.name;
    }

    public void setChaScore(int chaScore) {
        this.chaScore = chaScore;
        this.chaMod = getModifier(chaScore);
    }

    public int getChaMod() {
        return chaMod;
    }

    public void setChaMod(int chaMod) {
        this.chaMod = chaMod;
    }

    public int getComScore() {
        return comScore;
    }

    public void setComScore(int comScore) {
        this.comScore = comScore;
        this.comMod = getModifier(comScore);
    }

    public int getMaxHP() { return MaxHP;}
    public void setMaxHP(int NewMaxHP) {this.MaxHP = NewMaxHP;}
    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(currentHp, this.health);
    }

    // Getter and setter for rangedDamageDice
    public Dice getRangedDamageDice() {
        return rangedDamageDice;
    }

    public void setRangedDamageDice(Dice rangedDamageDice) {
        this.rangedDamageDice = rangedDamageDice;
    }

    // Getter and setter for closeDamageDice
    public Dice getCloseDamageDice() {
        return closeDamageDice;
    }

    public void setCloseDamageDice(Dice closeDamageDice) {
        this.closeDamageDice = closeDamageDice;
    }

    // Methods to calculate damage
    public int calculateRangedDamage() {
        return rangedDamageBonus + (rangedDamageDice != null ? rangedDamageDice.roll() : 0);
    }

    public int calculateCloseDamage() {
        return closeDamageBonus + (closeDamageDice != null ? closeDamageDice.roll() : 0);
    }

    public int getSwimspeed() { return swimspeed;}

    public void setSwimspeed(int newswimspeed) {this.swimspeed = newswimspeed;}

    public int getComMod() {
        return comMod;
    }

    public void setComMod(int comMod) {
        this.comMod = comMod;
    }

    public int getConScore() {
        return conScore;
    }

    public void setConScore(int conScore) {
        this.conScore = conScore;
        this.conMod = getModifier(conScore);
    }

    public int getConMod() {
        return conMod;
    }

    public void setConMod(int conMod) {
        this.conMod = conMod;
    }

    public int getDexScore() {
        return dexScore;
    }

    public void setDexScore(int dexScore) {
        this.dexScore = dexScore;
        this.dexMod = getModifier(dexScore);
    }

    public int getDexMod() {
        return dexMod;
    }

    public void setDexMod(int dexMod) {
        this.dexMod = dexMod;
    }

    public int getStrScore() {
        return strScore;
    }

    public void setStrScore(int strScore) {
        this.strScore = strScore;
        this.strMod = getModifier(strScore);
    }

    public int getStrMod() {
        return strMod;
    }

    public void setStrMod(int strMod) {
        this.strMod = strMod;
    }

    public int getIntScore() {
        return intScore;
    }

    public void setIntScore(int intScore) {
        this.intScore = intScore;
        this.intMod = getModifier(intScore);
    }

    public int getIntMod() {
        return intMod;
    }

    public void setIntMod(int intMod) {
        this.intMod = intMod;
    }

    public int getWisScore() {
        return wisScore;
    }

    public void setWisScore(int wisScore) {
        this.wisScore = wisScore;
        this.wisMod = getModifier(wisScore);
    }

    public int getWisMod() {
        return wisMod;
    }

    public void setWisMod(int wisMod) {
        this.wisMod = wisMod;
    }

    public int getUnspentSkillPoints() {
        return unspentSkillPoints;
    }

    public void setUnspentSkillPoints(int unspentSkillPoints) {
        this.unspentSkillPoints = unspentSkillPoints;
    }


    public void allocateSkillPoint(Skills.Skill skill) {
        if (unspentSkillPoints > 0) {
            skills.incrementSkill(skill);
            unspentSkillPoints--;
        }
    }
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
        // Recalculate damage bonuses if attackBonus changes
        this.rangedDamageBonus = this.attackBonus + this.dexMod;
        this.closeDamageBonus = this.attackBonus + this.strMod;
    }


    //Stuff about grabbing a players portrait
    public PlayerCharacter(Texture portrait) {
        this.portrait = portrait;
    }
        /// So this can get invoked for any player character class (duh, it's a method),
        /// so Guy2.Getportrait can be used in a ah who gives a shit
    public Texture getPortrait() {
        return portrait;
    }

    public void setPortrait(Texture portrait) {
        this.portrait = portrait;
    }

    //So this makes it easier to manage speaking, you can supply a dialogue like this:
    /// dialogueSystem.addDialogue(player.speak("I am the man"));
    /// So this'll take the player and have them speak the line given

    public Dialogue speak(String text) {
        return new Dialogue("PC", text, portrait);
    }

    public boolean isCanFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addResistance(DamageType damageType) {
        this.resistances.add(damageType);
    }

    public void addVulnerability(DamageType damageType) {
        this.vulnerabilities.add(damageType);
    }

    public void setHealth(int Health) {
        this.health = Health;
    }

    public int getHealth() {
        return this.health;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public int getReflexSave() {
        return reflexSave;
    }

    public void setReflexSave(int reflexSave) {
        this.reflexSave = reflexSave;
    }

    public int getCharmSave() {
        return charmSave;
    }

    public void setCharmSave(int charmSave) {
        this.charmSave = charmSave;
    }

    public int getToughnessSave() {
        return toughnessSave;
    }

    public void setToughnessSave(int toughnessSave) {
        this.toughnessSave = toughnessSave;
    }

    public int getLogicSave() {
        return logicSave;
    }

    public void setLogicSave(int logicSave) {
        this.logicSave = logicSave;
    }

    public int getWillSave() {
        return willSave;
    }

    public void setWillSave(int willSave) {
        this.willSave = willSave;
    }

    public int getPowerSave() {
        return powerSave;
    }

    public void setPowerSave(int powerSave) {
        this.powerSave = powerSave;
    }

    public int getLooksSave() {
        return looksSave;
    }

    public void setLooksSave(int looksSave) {
        this.looksSave = looksSave;
    }

    public int getRangedDamageBonus() {
        return rangedDamageBonus;
    }

    public void setRangedDamageBonus(int rangedDamageBonus) {
        this.rangedDamageBonus = rangedDamageBonus;
    }

    // Getter and setter for closeDamageBonus
    public int getCloseDamageBonus() {
        return closeDamageBonus;
    }

    public void setCloseDamageBonus(int closeDamageBonus) {
        this.closeDamageBonus = closeDamageBonus;
    }

    public String getArmorType() {
        return armorType;
    }

    public void setArmorType(String armorType) {
        this.armorType = armorType;
        setArmorProperties(armorType);
    }

    // Getter for armorDamageNegation
    public int getArmorDamageNegation() {
        return armorDamageNegation;
    }

    // Getter for armorDefensePenalty
    public int getArmorDefensePenalty() {
        return armorDefensePenalty;
    }

    // Method to set armor properties based on armor type
    private void setArmorProperties(String armorType) {
        switch (armorType) {
            case "Ultralight":
                this.ArmourDie = new Dice(1);   // always rolls 1
                this.armorDefensePenalty = 0;
                break;
            case "Very Light":
                this.ArmourDie = new Dice(2);
                this.armorDefensePenalty = 0;
                break;
            case "Light":
                this.ArmourDie = new Dice(3);
                this.armorDefensePenalty = 0;
                break;
            case "Medium":
                this.ArmourDie = new Dice(4);
                this.armorDefensePenalty = 0;
                break;
            case "Heavy":
                this.ArmourDie = new Dice(6);
                this.armorDefensePenalty = 1;
                break;
            case "Very Heavy":
                this.ArmourDie = new Dice(8);
                this.armorDefensePenalty = 1;
                break;
            case "Ultra Heavy":
                this.ArmourDie = new Dice(10);
                this.armorDefensePenalty = 2;
                break;
            case "Juggernaut":
                this.ArmourDie = new Dice(12);
                this.armorDefensePenalty = 3;
                break;
            default:
                this.ArmourDie = null;
                this.armorDefensePenalty = 0;
                break;
        }
    }



    private int rollDamageDie(int sides) {
        Dice dice = new Dice(sides);
        return dice.roll();
    }

    // Method to calculate damage after applying armor and defense penalties
    public int calculateDamage(int baseDamage) {
        int damageAfterArmor = Math.max(baseDamage - armorDamageNegation, 0);
        return damageAfterArmor;
    }

    // Method to apply defense penalties
    public void applyDefensePenalties() {
        this.rangedDefence = Math.max(this.rangedDefence - armorDefensePenalty, 0);
        this.closeDefence = Math.max(this.closeDefence - armorDefensePenalty, 0);
    }


    // Getter and setter for attackBonus

    public void receiveDamage(int damage, DamageType damageType) {

        // Armour: roll per hit
        if (ArmourDie != null) {
            int negation = ArmourDie.roll();
            damage = Math.max(damage - negation, 0);
        }

        // Resistances / vulnerabilities
        if (resistances.contains(damageType)) {
            damage /= 2;
        } else if (vulnerabilities.contains(damageType)) {
            damage *= 2;
        }

        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }


    public boolean isCanFitThroughSmallSpaces() {
        return canFitThroughSmallSpaces;
    }

    public void setCanFitThroughSmallSpaces(boolean canFitThroughSmallSpaces) {
        this.canFitThroughSmallSpaces = canFitThroughSmallSpaces;
    }

    public int getLevel() {
        return level;
    }

    // Getter for experience points
    public int getExperiencePoints() {
        return experiencePoints;
    }
}


