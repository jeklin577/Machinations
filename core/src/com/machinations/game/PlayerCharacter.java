package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlayerCharacter {

    // ---------- Static / shared ----------
    private static Texture defaultPortrait;

    private static Texture getDefaultPortrait() {
        if (defaultPortrait == null) {
            defaultPortrait = new Texture(Gdx.files.internal("Portraits/RebutPortrait.png"));
        }
        return defaultPortrait;
    }

    private static int halfRoundUp(int value) {
        return (value + 1) / 2;
    }

    // ---------- Identity ----------
    private String name;
    private String species;
    private String classType;

    // ---------- Progression ----------
    private int level;
    private int experiencePoints;
    private int attackBonus;

    // ---------- HP ----------
    private int maxHp;
    private int currentHp;

    // ---------- Portrait ----------
    private Texture portrait;

    // ---------- Core stats ----------
    private int strScore;
    private int dexScore;
    private int conScore;
    private int intScore;
    private int wisScore;
    private int chaScore;
    private int comScore;

    // ---------- Modifiers ----------
    private int strMod;
    private int dexMod;
    private int conMod;
    private int intMod;
    private int wisMod;
    private int chaMod;
    private int comMod;

    // ---------- Derived saves ----------
    private int reflexSave;
    private int charmSave;
    private int toughnessSave;
    private int logicSave;
    private int willSave;
    private int powerSave;
    private int looksSave;

    // ---------- Derived combat ----------
    private int rangedDefence;
    private int closeDefence;
    private int rangedDamageBonus;
    private int closeDamageBonus;

    // ---------- Movement / traits ----------
    private boolean canFly;
    private boolean canFitThroughSmallSpaces;
    private int speed;
    private int swimspeed;

    // ---------- Armour ----------
    private String armorType;
    private int armorDefensePenalty;
    private Dice armourDie;

    // ---------- Damage / resistances ----------
    private Dice rangedDamageDice;
    private Dice closeDamageDice;
    private final Set<DamageType> resistances = new HashSet<>();
    private final Set<DamageType> vulnerabilities = new HashSet<>();

    // ---------- Skills / class ----------
    private int unspentSkillPoints;
    private Skills skills;
    private final ClassLevelProgression classLevelProgression;

    // ---------- Race traits ----------
    private ArrayList<RaceTrait> raceTraits = new ArrayList<>();

    // ---------- Base state for rebuild / preview ----------
    private int baseStrScore;
    private int baseDexScore;
    private int baseConScore;
    private int baseIntScore;
    private int baseWisScore;
    private int baseChaScore;
    private int baseComScore;

    private int baseStrMod;
    private int baseDexMod;
    private int baseConMod;
    private int baseIntMod;
    private int baseWisMod;
    private int baseChaMod;
    private int baseComMod;

    private int baseReflexSave;
    private int baseCharmSave;
    private int baseToughnessSave;
    private int baseLogicSave;
    private int baseWillSave;
    private int basePowerSave;
    private int baseLooksSave;

    private int baseRangedDefence;
    private int baseCloseDefence;
    private int baseRangedDamageBonus;
    private int baseCloseDamageBonus;

    private int baseAttackBonus;
    private int baseUnspentSkillPoints;
    private Skills baseSkills;

    private int baseMaxHp;
    private int baseCurrentHp;

    private boolean baseCanFly;
    private boolean baseCanFitThroughSmallSpaces;
    private int baseSpeed;
    private int baseSwimspeed;

    private Set<DamageType> baseResistances = new HashSet<>();
    private Set<DamageType> baseVulnerabilities = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructor
    // NOTE: constructor order is now:
    // str, dex, con, int, wis, cha, com
    // -------------------------------------------------------------------------
    public PlayerCharacter(String name,
                           String species,
                           String classType,
                           ClassLevelProgression classLevelProgression,
                           int strScore,
                           int dexScore,
                           int conScore,
                           int intScore,
                           int wisScore,
                           int chaScore,
                           int comScore) {

        this.name = name;
        this.species = species;
        this.classType = classType;
        this.classLevelProgression = classLevelProgression;

        this.level = 1;
        this.experiencePoints = 0;
        this.attackBonus = 0;

        this.skills = new Skills();
        initializeSkills();

        this.canFly = false;
        this.canFitThroughSmallSpaces = false;
        this.speed = 10;
        this.swimspeed = this.speed / 2;

        this.portrait = getDefaultPortrait();

        this.strScore = strScore;
        this.dexScore = dexScore;
        this.conScore = conScore;
        this.intScore = intScore;
        this.wisScore = wisScore;
        this.chaScore = chaScore;
        this.comScore = comScore;

        recalculateCoreDerivedStats();

        this.maxHp = rollStartingHp();
        this.currentHp = this.maxHp;

        captureBaseState();
    }

    // -------------------------------------------------------------------------
    // Initialisation helpers
    // -------------------------------------------------------------------------
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

    private void initializeGeneralSkills() {
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

    // -------------------------------------------------------------------------
    // Core derived stat calculation
    // -------------------------------------------------------------------------
    private void recalculateCoreDerivedStats() {
        this.strMod = getModifier(strScore);
        this.dexMod = getModifier(dexScore);
        this.conMod = getModifier(conScore);
        this.intMod = getModifier(intScore);
        this.wisMod = getModifier(wisScore);
        this.chaMod = getModifier(chaScore);
        this.comMod = getModifier(comScore);

        this.reflexSave = halfRoundUp(dexScore);
        this.charmSave = halfRoundUp(chaScore);
        this.toughnessSave = halfRoundUp(conScore);
        this.logicSave = halfRoundUp(intScore);
        this.willSave = halfRoundUp(wisScore);
        this.powerSave = halfRoundUp(strScore);
        this.looksSave = halfRoundUp(comScore);

        this.rangedDefence = 12 + dexMod;
        this.closeDefence = 12 + conMod;

        // MOTSP: ranged damage uses Wisdom bonus, close damage uses Strength bonus
        this.rangedDamageBonus = wisMod;
        this.closeDamageBonus = strMod;
    }

    private int getModifier(int stat) {
        return (stat - 10) / 2;
    }

    // -------------------------------------------------------------------------
    // HP rules
    // -------------------------------------------------------------------------
    private int getClassHitDieSides() {
        switch (classType) {
            case "Killer":
                return 8;
            case "Expert":
            case "Scholar":
            case "Psion":
            default:
                return 6;
        }
    }

    private int getFixedHpGainAfterThreshold() {
        switch (classType) {
            case "Killer":
                return 3;
            case "Expert":
                return 2;
            case "Psion":
                return 2;
            case "Scholar":
            default:
                return 1;
        }
    }

    private int getLevelWhereConStopsApplying() {
        switch (classType) {
            case "Killer":
            case "Psion":
                return 10;
            case "Expert":
            case "Scholar":
            default:
                return 11;
        }
    }

    private int rollStartingHp() {
        int hp = new Dice(getClassHitDieSides()).roll() + conMod;
        return Math.max(1, hp);
    }

    private int getHpGainForNewLevel(int newLevel) {
        int gain;

        if (newLevel >= getLevelWhereConStopsApplying()) {
            gain = getFixedHpGainAfterThreshold();
        } else {
            gain = new Dice(getClassHitDieSides()).roll() + conMod;
        }

        return Math.max(1, gain);
    }

    private void levelUp() {
        level++;
        int hpGain = getHpGainForNewLevel(level);
        maxHp += hpGain;
        currentHp += hpGain;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    // -------------------------------------------------------------------------
    // Trait rebuild / preview support
    // -------------------------------------------------------------------------
    public void captureBaseState() {
        this.baseStrScore = this.strScore;
        this.baseDexScore = this.dexScore;
        this.baseConScore = this.conScore;
        this.baseIntScore = this.intScore;
        this.baseWisScore = this.wisScore;
        this.baseChaScore = this.chaScore;
        this.baseComScore = this.comScore;

        this.baseStrMod = this.strMod;
        this.baseDexMod = this.dexMod;
        this.baseConMod = this.conMod;
        this.baseIntMod = this.intMod;
        this.baseWisMod = this.wisMod;
        this.baseChaMod = this.chaMod;
        this.baseComMod = this.comMod;

        this.baseReflexSave = this.reflexSave;
        this.baseCharmSave = this.charmSave;
        this.baseToughnessSave = this.toughnessSave;
        this.baseLogicSave = this.logicSave;
        this.baseWillSave = this.willSave;
        this.basePowerSave = this.powerSave;
        this.baseLooksSave = this.looksSave;

        this.baseRangedDefence = this.rangedDefence;
        this.baseCloseDefence = this.closeDefence;
        this.baseRangedDamageBonus = this.rangedDamageBonus;
        this.baseCloseDamageBonus = this.closeDamageBonus;

        this.baseAttackBonus = this.attackBonus;
        this.baseUnspentSkillPoints = this.unspentSkillPoints;
        this.baseSkills = this.skills.copy();

        this.baseMaxHp = this.maxHp;
        this.baseCurrentHp = this.currentHp;

        this.baseCanFly = this.canFly;
        this.baseCanFitThroughSmallSpaces = this.canFitThroughSmallSpaces;
        this.baseSpeed = this.speed;
        this.baseSwimspeed = this.swimspeed;

        this.baseResistances = new HashSet<>(this.resistances);
        this.baseVulnerabilities = new HashSet<>(this.vulnerabilities);
    }

    public void resetToBaseState() {
        this.strScore = baseStrScore;
        this.dexScore = baseDexScore;
        this.conScore = baseConScore;
        this.intScore = baseIntScore;
        this.wisScore = baseWisScore;
        this.chaScore = baseChaScore;
        this.comScore = baseComScore;

        this.strMod = baseStrMod;
        this.dexMod = baseDexMod;
        this.conMod = baseConMod;
        this.intMod = baseIntMod;
        this.wisMod = baseWisMod;
        this.chaMod = baseChaMod;
        this.comMod = baseComMod;

        this.reflexSave = baseReflexSave;
        this.charmSave = baseCharmSave;
        this.toughnessSave = baseToughnessSave;
        this.logicSave = baseLogicSave;
        this.willSave = baseWillSave;
        this.powerSave = basePowerSave;
        this.looksSave = baseLooksSave;

        this.rangedDefence = baseRangedDefence;
        this.closeDefence = baseCloseDefence;
        this.rangedDamageBonus = baseRangedDamageBonus;
        this.closeDamageBonus = baseCloseDamageBonus;

        this.attackBonus = baseAttackBonus;
        this.unspentSkillPoints = baseUnspentSkillPoints;
        this.skills = baseSkills.copy();

        this.maxHp = baseMaxHp;
        this.currentHp = Math.min(baseCurrentHp, baseMaxHp);

        this.canFly = baseCanFly;
        this.canFitThroughSmallSpaces = baseCanFitThroughSmallSpaces;
        this.speed = baseSpeed;
        this.swimspeed = baseSwimspeed;

        this.resistances.clear();
        this.resistances.addAll(baseResistances);

        this.vulnerabilities.clear();
        this.vulnerabilities.addAll(baseVulnerabilities);
    }

    public void setRaceTraits(ArrayList<RaceTrait> traits) {
        this.raceTraits = (traits == null) ? new ArrayList<>() : new ArrayList<>(traits);
    }

    public ArrayList<RaceTrait> getRaceTraits() {
        return new ArrayList<>(raceTraits);
    }

    public void recalculateFromTraits() {
        resetToBaseState();

        if (raceTraits == null) return;

        for (RaceTrait trait : raceTraits) {
            trait.applyEffect(this);
        }
    }

    // -------------------------------------------------------------------------
    // XP
    // -------------------------------------------------------------------------
    public void addExperience(int amount) {
        experiencePoints += amount;

        LevelProgression progression = classLevelProgression.getProgressionForClassAndLevel(classType, level);
        while (progression != null && experiencePoints >= progression.getExperienceForNextLevel()) {
            experiencePoints -= progression.getExperienceForNextLevel();
            levelUp();
            progression = classLevelProgression.getProgressionForClassAndLevel(classType, level);
        }
    }

    // -------------------------------------------------------------------------
    // Combat / damage
    // -------------------------------------------------------------------------
    public int calculateRangedDamage() {
        return rangedDamageBonus + (rangedDamageDice != null ? rangedDamageDice.roll() : 0);
    }

    public int calculateCloseDamage() {
        return closeDamageBonus + (closeDamageDice != null ? closeDamageDice.roll() : 0);
    }

    public void receiveDamage(int damage, DamageType damageType) {
        if (armourDie != null) {
            int negation = armourDie.roll();
            damage = Math.max(damage - negation, 0);
        }

        if (resistances.contains(damageType)) {
            damage /= 2;
        } else if (vulnerabilities.contains(damageType)) {
            damage *= 2;
        }

        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + Math.max(0, amount));
    }

    // -------------------------------------------------------------------------
    // Armour
    // -------------------------------------------------------------------------
    public void setArmorType(String armorType) {
        this.armorType = armorType;

        switch (armorType) {
            case "Ultralight":
                this.armourDie = new Dice(1);
                this.armorDefensePenalty = 0;
                break;
            case "Very Light":
                this.armourDie = new Dice(2);
                this.armorDefensePenalty = 0;
                break;
            case "Light":
                this.armourDie = new Dice(3);
                this.armorDefensePenalty = 0;
                break;
            case "Medium":
                this.armourDie = new Dice(4);
                this.armorDefensePenalty = 0;
                break;
            case "Heavy":
                this.armourDie = new Dice(6);
                this.armorDefensePenalty = 1;
                break;
            case "Very Heavy":
                this.armourDie = new Dice(8);
                this.armorDefensePenalty = 1;
                break;
            case "Ultra Heavy":
                this.armourDie = new Dice(10);
                this.armorDefensePenalty = 2;
                break;
            case "Juggernaut":
                this.armourDie = new Dice(12);
                this.armorDefensePenalty = 3;
                break;
            default:
                this.armourDie = null;
                this.armorDefensePenalty = 0;
                break;
        }
    }

    // -------------------------------------------------------------------------
    // Dialogue convenience
    // -------------------------------------------------------------------------
    public Dialogue speak(String text) {
        return new Dialogue("PC", text, portrait);
    }

    // -------------------------------------------------------------------------
    // Getters / setters
    // -------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getClassType() {
        return classType;
    }

    public int getLevel() {
        return level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public Texture getPortrait() {
        return portrait;
    }

    public void setPortrait(Texture portrait) {
        this.portrait = portrait;
    }

    public int getStrScore() {
        return strScore;
    }

    public void setStrScore(int strScore) {
        this.strScore = strScore;
        this.strMod = getModifier(strScore);
        this.powerSave = halfRoundUp(strScore);
        this.closeDamageBonus = this.strMod;
    }

    public int getDexScore() {
        return dexScore;
    }

    public void setDexScore(int dexScore) {
        this.dexScore = dexScore;
        this.dexMod = getModifier(dexScore);
        this.reflexSave = halfRoundUp(dexScore);
        this.rangedDefence = 12 + this.dexMod;
    }

    public int getConScore() {
        return conScore;
    }

    public void setConScore(int conScore) {
        this.conScore = conScore;
        this.conMod = getModifier(conScore);
        this.toughnessSave = halfRoundUp(conScore);
        this.closeDefence = 12 + this.conMod;
    }

    public int getIntScore() {
        return intScore;
    }

    public void setIntScore(int intScore) {
        this.intScore = intScore;
        this.intMod = getModifier(intScore);
        this.logicSave = halfRoundUp(intScore);
    }

    public int getWisScore() {
        return wisScore;
    }

    public void setWisScore(int wisScore) {
        this.wisScore = wisScore;
        this.wisMod = getModifier(wisScore);
        this.willSave = halfRoundUp(wisScore);
        this.rangedDamageBonus = this.wisMod;
    }

    public int getChaScore() {
        return chaScore;
    }

    public void setChaScore(int chaScore) {
        this.chaScore = chaScore;
        this.chaMod = getModifier(chaScore);
        this.charmSave = halfRoundUp(chaScore);
    }

    public int getComScore() {
        return comScore;
    }

    public void setComScore(int comScore) {
        this.comScore = comScore;
        this.comMod = getModifier(comScore);
        this.looksSave = halfRoundUp(comScore);
    }

    public int getStrMod() {
        return strMod;
    }

    public int getDexMod() {
        return dexMod;
    }

    public int getConMod() {
        return conMod;
    }

    public int getIntMod() {
        return intMod;
    }

    public int getWisMod() {
        return wisMod;
    }

    public int getChaMod() {
        return chaMod;
    }

    public int getComMod() {
        return comMod;
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

    public int getRangedDefence() {
        return rangedDefence;
    }

    public void setRangedDefence(int rangedDefence) {
        this.rangedDefence = rangedDefence;
    }

    public int getCloseDefence() {
        return closeDefence;
    }

    public void setCloseDefence(int closeDefence) {
        this.closeDefence = closeDefence;
    }

    public int getRangedDamageBonus() {
        return rangedDamageBonus;
    }

    public void setRangedDamageBonus(int rangedDamageBonus) {
        this.rangedDamageBonus = rangedDamageBonus;
    }

    public int getCloseDamageBonus() {
        return closeDamageBonus;
    }

    public void setCloseDamageBonus(int closeDamageBonus) {
        this.closeDamageBonus = closeDamageBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
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

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills.copy();
    }

    public int getMaxHP() {
        return maxHp;
    }

    public void setMaxHP(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
        if (currentHp > this.maxHp) {
            currentHp = this.maxHp;
        }
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, Math.min(currentHp, maxHp));
    }

    // Compatibility wrappers
    public int getHealth() {
        return currentHp;
    }

    public void setHealth(int health) {
        setCurrentHp(health);
    }

    public Dice getRangedDamageDice() {
        return rangedDamageDice;
    }

    public void setRangedDamageDice(Dice rangedDamageDice) {
        this.rangedDamageDice = rangedDamageDice;
    }

    public Dice getCloseDamageDice() {
        return closeDamageDice;
    }

    public void setCloseDamageDice(Dice closeDamageDice) {
        this.closeDamageDice = closeDamageDice;
    }

    public boolean isCanFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public boolean isCanFitThroughSmallSpaces() {
        return canFitThroughSmallSpaces;
    }

    public void setCanFitThroughSmallSpaces(boolean canFitThroughSmallSpaces) {
        this.canFitThroughSmallSpaces = canFitThroughSmallSpaces;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSwimspeed() {
        return swimspeed;
    }

    public void setSwimspeed(int swimspeed) {
        this.swimspeed = swimspeed;
    }

    public String getArmorType() {
        return armorType;
    }

    public int getArmorDefensePenalty() {
        return armorDefensePenalty;
    }

    public Dice getArmourDie() {
        return armourDie;
    }

    public void addResistance(DamageType damageType) {
        resistances.add(damageType);
    }

    public void addVulnerability(DamageType damageType) {
        vulnerabilities.add(damageType);
    }

    public Set<DamageType> getResistances() {
        return new HashSet<>(resistances);
    }

    public Set<DamageType> getVulnerabilities() {
        return new HashSet<>(vulnerabilities);
    }
}