package com.machinations.game;

public abstract class RaceTrait {
    public abstract void applyEffect(PlayerCharacter playerCharacter);
    public abstract String getDescription();



    public static boolean isHumanoidTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Adaptable.class || traitClass == Fecund.class;
    }

    public static boolean isAmmoniaBasedTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Gasbag.class;
    }

    public static boolean isAmoeboidTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == LimitedShapeshifting.class;
    }

    public static boolean isGiantTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Resilient.class || traitClass == Strong.class || traitClass == Enduring.class;
    }


    public static boolean isHerbivoreTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Fearful.class || traitClass == RapidReaction.class;
    }

    public static boolean isHighGravityTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == VeryStrong.class || traitClass == VeryTough.class || traitClass == HiGAdapted.class;
    }

    public static boolean isHunterTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Survival.class || traitClass == Stalker.class;
    }

    public static boolean isInsectTrait(Class<? extends RaceTrait> traitClass) {
        return traitClass == Flight.class;
    }


    public static class Adaptable extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setUnspentSkillPoints(playerCharacter.getUnspentSkillPoints() + 1);
        }

        @Override
        public String getDescription() {
            return "'Specialisation is for insects' Gain +1 skill Point.";
        }
    }

    //No Vitals: Half hit-points. Take only one damage from physical attacks. Take normal damage from energy attacks

    public static class NoVitals extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.addResistance(DamageType.PHYSICAL);
            //PLACEHOLDER IMPLEMENT REDUCING PHYS DAMAGE TO 1 (Maybe when combat implemented?)
        }

        @Override
        public String getDescription() {
            return "Your heart has a negative BPM, you take one damage from physial attacks, but take normal damage from energy, and have halved HP.";
        }
    }

    public static class Ascetic extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.INVESTMENT,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.INVESTMENT) + 1);
        }

        @Override
        public String getDescription() {
            return "'Mo' Money, Mo' problems' - Hallowed Monk Nwa Big, Gain +1 to your investment skill.";
        }
    }





    public static class Swimming extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setSwimspeed(playerCharacter.getSwimspeed()*2);
        }

        @Override
        public String getDescription() {
            return "'Sleep with the fishes' to you just means a good night's rest, your swimming speed is doubled.";
        }
    }

    public static class KeenSight extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.SEARCH,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.SEARCH) + 1);
        }

        @Override
        public String getDescription() {
            return "You've got an Eye for treasures, traps, faces in a crowd, everything really, gain +1 to your search skill.";
        }
    }

    public static class LightBody extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            ///Trade Con for Dex, amybe just a big con penalty and dex bonus?
        }

        @Override
        public String getDescription() {
            return "A tree is sturdy, but a bird is swift, who do you think gets more fruit? Gain a penalty to Con in exchange for a large increase in dexterity";
        }
    }

    public static class NaturalWeapons extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            ///add a weapon to player inventory, that can't be unequppied or something?
        }

        @Override
        public String getDescription() {
            return "Your third leg is a knife. Gain a natural weapons that dealth D4 damange, every time you take this trait, increase the die by one tier (4,6,8,12,20)";
        }
    }

    public static class RadResist extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.addResistance(DamageType.ENERGY);
        }

        @Override
        public String getDescription() {
            return "Radioactive? No, Radiodocile. gain Resistance to radiation (and energy damage)";
        }
    }

    public static class Rocky extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //Increase armour rolls by +1
        }

        @Override
        public String getDescription() {
            return "The idea of a 'Mountain King' is more literal for your species. Increase your armour rolls by +1";
        }
    }

    public static class Consort extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setStrScore(playerCharacter.getStrScore() + 1);
            playerCharacter.setConScore(playerCharacter.getConScore() + 2);
            playerCharacter.setIntScore(playerCharacter.getIntScore() - 1);
            playerCharacter.setWisScore(playerCharacter.getWisScore() - 1);
        }

        @Override
        public String getDescription() {
            return "In the complex caste system of your homeworld, you are a himbo. Gain +1 Strength and +2 Constitution, at the cost of -1 Inteliigence and Wisdom.";
        }
    }

    public static class Monarch extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setStrScore(playerCharacter.getStrScore() + 2);
            playerCharacter.setConScore(playerCharacter.getConScore() + 2);
            playerCharacter.setDexScore(playerCharacter.getDexScore() - 3);
        }

        @Override
        public String getDescription() {
            return "In the complex caste system of your homeworld, you may be a living god, undisputed sovereign, or duly elected president imbued with the will of the people. you gain +2 Strength and Constitution, at the cost of -3 Dexterity";
        }
    }

    public static class Soldier extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setStrScore(playerCharacter.getStrScore() + 1);
            playerCharacter.setConScore(playerCharacter.getConScore() + 1);
            playerCharacter.setWisScore(playerCharacter.getWisScore() - 1);
            playerCharacter.setIntScore(playerCharacter.getIntScore() - 1);
            ///Maybe add some kind of "Military heritage" for bonsuses in coversations/certain checks thing to make this better than the Monarch?
        }

        @Override
        public String getDescription() {
            return "In the complex caste system of your homeworld, you are expendable laser fodder, you gain + 1 Strength and Constitution, at the cost of -1 Intelligence and Wisdom";
        }
    }

    public static class Worker extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setUnspentSkillPoints(playerCharacter.getUnspentSkillPoints() + 1);
            playerCharacter.setConScore(playerCharacter.getConScore() + 1);

            ///Maybe add some kind of "low class origin" thing to make certain factions pissed off at you in order to make this not objectively superior to adaptable?
        }

        @Override
        public String getDescription() {
            return "In the complex caste system of your homeworld, you do the vast majority of the work, and are therefore treated terrible, gain +1 Skill & Constitution point";
        }
    }

    public static class Camoflague extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            ///find a way to only apply this bonus if Naked?
            playerCharacter.getSkills().setSkillValue(Skills.Skill.STEALTH,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.STEALTH) + 1);


        }

        @Override
        public String getDescription() {
            return "Your Species has the remarkable ability to shift hue to blend in with your surroundings, your clothes have stubbornly refused to get with the program, gain +2 to stealth rolls while naked.";
        }
    }

    public static class Ambush extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.SNEAK_ATTACK,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.SNEAK_ATTACK) + 1);
        }

        @Override
        public String getDescription() {
            return "You're well acquainted with the honourable art of shooting an opponent in the head while they're distracted, gain +1 to your Sneak Attack Skill.";
        }
    }

    public static class Intrusive extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.SECURITY,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.SECURITY) + 1);
        }

        @Override
        public String getDescription() {
            return "What is a lock if not a challenge? Gain +1 to your Security Skill.";
        }
    }

    public static class Sneaky extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.STEALTH,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.STEALTH) + 1);
        }

        @Override
        public String getDescription() {
            return "'Step one of a life of crime: Don't get caught.' - Krezzik, Master Thief, you gain +1 to your stealth skill.";
        }
    }

    public static class TechSavant extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.TINKER,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.TINKER) + 1);
        }

        @Override
        public String getDescription() {
            return "Gadgets, Gizmos, Doohickeys and thingamajigs all fall before your expertise, gain +1 to your Tinker skill.";
        }
    }

    public static class Wary extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setReflexSave(playerCharacter.getToughnessSave() + 1);
            playerCharacter.setLogicSave(playerCharacter.getPowerSave() + 1);
        }

        @Override
        public String getDescription() {
            return "You've seen enough bad ideas to see them coming, and how to manage their consequences, gain +1 to your reflexes and logic saves.";
        }
    }

    public static class DeadFlesh extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            /// Double player HP
            /// Add some kind of consequnce mechanic for this, lower comeliness and cha, maybe some kind of flag to make certain factions see you as a monster
        }

        @Override
        public String getDescription() {
            return "You once heard someone saying some people 'just lay down and rot', you felt seen, your decaying flesh body doubles your HP, but at the cost of (define the penalty later)";
        }
    }

    public static class Confident extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setWillSave(playerCharacter.getWillSave() + 2);
        }

        @Override
        public String getDescription() {
            return "You're a very confident person, with much to be confident about. Gain +2 to Will saves.";
        }
    }

    public static class Excess extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setConScore(playerCharacter.getConScore() + 2);
            playerCharacter.setWisScore(playerCharacter.getWisScore() - 1);
        }

        @Override
        public String getDescription() {
            return "Moderation is for cowards, so eat, drink and make merry, for tomorrow you'll probably get someone to die for you gain + 2 Constition and -1 Wisdom.";
        }
    }

    public static class Fashionable extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setConScore(playerCharacter.getConScore() + 2);
            playerCharacter.setWisScore(playerCharacter.getWisScore() - 1);
        }

        @Override
        public String getDescription() {
            return "You dress to impress, gain +1 to your looks and charm saves";
        }
    }

    public static class Shell extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            ///Add natural armour here, same kind of logic as the natural weapons, we'll add these when we're done with inventory shit
        }

        @Override
        public String getDescription() {
            return "By definition, you're a very guarded person. You gain natural armour of 1d4, this increases each time you take this trait.";
        }
    }

    public static class Spines extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
           /// Enemies must make a reflex save or take a d4 of damage, add this when Combat's in
        }

        @Override
        public String getDescription() {
            return "Anyone trying to grab you is getting a hand full of barbs, your enemies must make a reflex save when they attack you with a melee weapon, and take 1d4 damage if they fail.";
        }
    }

    public static class Tourist extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //WRITE THIS EFFECT IN LATER WE'RE LAZY AS SHIT
        }

        @Override
        public String getDescription() {
            return "You're not from' round these parts, but you've been keen to learn, gain +1 to ExoTech and Xenoarcheaology, at the cost of -2 Charisma. ";
        }
    }

    public static class Linguist extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //WRITE THIS EFFECT IN LATER WE'RE LAZY AS SHIT
        }

        @Override
        public String getDescription() {
            return "Rediscovering language from first principles is a suprisingly good way to get into peoples heads, gain +1 to languages and xenopsych, at the cost of someshit idunno this is unfniished";
        }
    }

    public static class Cunning extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setWisScore(playerCharacter.getWisScore() + 1);
        }

        @Override
        public String getDescription() {
            return "Nobody could ever call you 'Cun't' Gain +1 Wisdom ";
        }
    }

    //elemental traits, these count as 2 race traits when selected, will figure this out later
    public static class Air extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setDexScore(playerCharacter.getDexScore() + 1);
            playerCharacter.setWillSave(playerCharacter.getWillSave() + 2);
        }

        @Override
        public String getDescription() {
            return "Out of the four classical elements, you're the best, you outnumber the others by far, gain +1 dex and +2 to your will saves";
        }
    }

    public static class Fire extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
           /// gain nautral weapon that deals 1d4 fire damage, use the spines mechanic but with toughness not reflexes for 1d4 fire dam
            playerCharacter.addResistance(DamageType.HEAT);
            playerCharacter.addVulnerability(DamageType.COLD);
        }

        @Override
        public String getDescription() {
            return "Out of the four classical elements, you're the best, you burn brighter and can destroy the others. Gain a 1d4 natural fire weapon, resistance to heat damage, and vulnerability to cold damage.";
        }
    }

    public static class Earth extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
           /// Increase armour rolls by +1
            //+1 con (too fucking lazy to do it)
        }

        @Override
        public String getDescription() {
            return "Out of the four classical elements, you're the best, long after the others are gone, your mountainous mass will remain, you gain +1 con, and a +1 to all armour rolls.";
        }
    }

    public static class Water extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            /// increase str by 1
            //+1 con (too fucking lazy to do it)
        }

        @Override
        public String getDescription() {
            return "Out of the four classical elements, you're the best, you can erode and douse any challengers, gain +1 constitution and strength.";
        }
    }

    public static class Energistic extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.addResistance(DamageType.ENERGY);
            playerCharacter.addVulnerability(DamageType.PHYSICAL);
        }

        @Override
        public String getDescription() {
            return "Hippies tend to confuse you and vibes a lot, gain a resistance to energy damage, and a vulnerability to physical.";
        }
    }

    /// Exotic Attack (Exotic)
    /// You have access to a special, exotic attack, unique to your species which you can use once per encounter.
    /// Dart: You can shoot a dart or quill from your flesh as a ranged attack with a range equal to your Strength in metres. It does
    /// D4 damage. You can take this multiple times to step up the damage and to increase the range by 2 metres each time.
    /// Electroshock: You can discharge an electric shock through your body. This is a Hand-to-Hand attack, resisted by a
    /// Toughness Save. If they fail they are stunned for 1d6 turns.
    /// Fire Breathing: You can breathe a plume of fire up to five metres. This does D4 damage to anyone hit by it. They can make
    /// a Reflexes Save to dodge. You can take this multiple times to step up the damage.
    /// Sonic Boom: You can discharge a powerful sound at a range of up to five metres. This does D4 damage to anyone hit by it.
    /// They can make a Toughness Save to resist the effect. You can take this multiple times to step up the damage.
    /// Toxic Gas: You can expel a cloud of toxic gas two metres around yourself in every direction. Those in the cloud must make a
    /// Toughness save or take D4 damage. You can step this up by taking it multiple times. You are unaffected by the cloud and it
    /// can last several turns.
    ///Ok so these are the exotic attacks, i have no idea if i can implement these properly

    public static class Graceful extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setDexScore(playerCharacter.getDexScore() + 1);
        }

        @Override
        public String getDescription() {
            return "When the television show named Earth was being created, they based cats partly off you, gain +1 dexterity";
        }
    }

    public static class Large extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            /// reduce ranged and close defence by 1, gain an extra +1 to melee strikes and health per level
        }

        @Override
        public String getDescription() {
            return "When you found out about Gittens, you genuinely couldn't believe a creature that small was real, your ranged and close defence is reduced by 1, but you gain +1 damage when hitting with a melee weapon, and an additional hitpoint per level";
        }
    }

    public static class Warrior extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            /// Plus one ranged and close attack (to hit chance)
        }

        @Override
        public String getDescription() {
            return "Your gait is the march, your music the bugle, and your destiny, an early grave, gain +1 to close and ranged attack.";
        }
    }

    public static class Weapon extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //Add profciency with a weapon type
        }

        @Override
        public String getDescription() {
            return "THIS IS MY RIFLE, THERE ARE MANY LIKE IT BUT THIS ONE IS MINE.";
        }
    }

    public static class Chitin extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //+1 to armour rolls
            //Might have some overlap with rocky/shell?
        }

        @Override
        public String getDescription() {
            return "Nature protects that which is precious, and it found you very precious, gain +1 to armour rolls.";
        }
    }

    public static class Flowering extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //+1 comeliness
        }

        @Override
        public String getDescription() {
            return "Aliens are suprisingly like bees in how attracted they get to pretty flowers, gain +1 Comeliness";
        }
    }

    public static class Foothands extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //+1 climb skill
        }

        @Override
        public String getDescription() {
            return "'Look ma! No feet!' You gain +1 to your climb skill.";
        }
    }

    public static class Tail extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //Gain extra attack if taken three times, and +1 reflex save generally
        }

        @Override
        public String getDescription() {
            return "'Getting Tail' is a phrase that confuses you, gives +1 reflex save, if taken three times, you can make an extra attack per round.";
        }
    }

    public static class PsiPower extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //gain an extra Psipoint to spend on abilities
        }

        @Override
        public String getDescription() {
            return "You have pierced through the veil to the psychic realm, all that's left is to reach out and take it, gain +1 Psi Point";
        }
    }

    public static class PsiTalent extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //gain an extra psi talent, even if not Psion(?)
            //this feels like it'll be a collosal pain in the ass to implement
        }

        @Override
        public String getDescription() {
            return "You give good brain. You can take a Psi power even if you're not a Psionic";
        }
    }

    public static class ColdBlooded extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
           //gain +1 will and toughness
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

    public static class Regeneration extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            //restore 1HP per round
        }

        @Override
        public String getDescription() {
            return "Your ancestors broke through concrete to gain nourishment from the stars, and you have similar tenacity, restore 1HP every round.";
        }
    }

    //Fuel eater's purely roleplaying based, eating the worst kind of stuff imaginable, but there's a few of these traits, maybe think about ways to implement them







    //So self protecting should go here but it's redundant with mindful, so i'm not writing it again
























    public static class Fecund extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setComScore(playerCharacter.getComScore() + 1);
        }

        @Override
        public String getDescription() {
            return "When people describe 'The Beast with two backs' They may well be referring to your species, you gain an extra Comeliness point";
        }
    }

    public static class Gasbag extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setCanFly(true);
            playerCharacter.setSpeed(playerCharacter.getSpeed() / 2);
        }
        @Override
        public String getDescription() {
            return "Your species developed a biological form of propulsion, you can fly naturally, but move half as fast.";
        }

    }

    public static class SlowMetabolism extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
           ///Implement after saving rolls are in
        }

        @Override
        public String getDescription() {
            return "Your Body's built to weed out Toxins and Disease, you gain a +5 to saving rolls against Poison and Disease";
        }
    }

    public static class LimitedShapeshifting extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setCanFitThroughSmallSpaces(true); // Example effect
        }
        @Override
        public String getDescription() {
            return "An intensive amount of flexibility allows you to easily contract yourself to fit in small spaces";
        }

    }
    public static class Resilient extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setToughnessSave(playerCharacter.getToughnessSave() + 1);
            playerCharacter.setPowerSave(playerCharacter.getPowerSave() + 1);
        }

        @Override
        public String getDescription() {
            return "You're built like a ToughCo certified Brick Shithouse, and gain +1 to your Toughness and Power Saves";
        }
    }
    public static class Enduring extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setConScore(playerCharacter.getConScore() + 1);
        }
        @Override
        public String getDescription() {
            return "'He Lives, and He lives, until all the lights go out' Gain +1 Constitution.";
        }

    }


    public static class Fearful extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setReflexSave(playerCharacter.getReflexSave() + 1);
            playerCharacter.setWillSave(playerCharacter.getWillSave() + 1);
        }

        @Override
        public String getDescription() {
            return "'Some may call it cowardice, but a rabbit who never burrows isn't courageous, they're stupid, and usually dead. Your fearful nature gives you +1 to your will and reflex saves.";
        }
    }

    public static class StrongWilled extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setWillSave(playerCharacter.getWillSave() + 2);
        }

        @Override
        public String getDescription() {
            return "Through continued cultivation of restraint, you are far harder to tempt, you gain +2 to your will saves.";
        }
    }

    public static class Tough extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setToughnessSave(playerCharacter.getToughnessSave() + 2);
        }

        @Override
        public String getDescription() {
            return "You are the waiting Anvil to the hammer of the world, and will not falter, gain +2 to toughnss saves. ";
        }
    }

    public static class Strong extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setStrScore(playerCharacter.getStrScore() + 1);
        }

        @Override
        public String getDescription() {
            return "'Eat any good books lately?' You gain +1 Strength";
        }
    }

    public static class RapidReaction extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setReflexSave(playerCharacter.getReflexSave() + 2);
        }

        @Override
        public String getDescription() {
            return "Docility is Death for your kind, and you have learned the lesson well. you gain +2 to reflex saves.";
        }
    }

    public static class VeryStrong extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setStrScore(playerCharacter.getStrScore() + 2);
            playerCharacter.setDexScore(playerCharacter.getDexScore() - 1);
        }

        @Override
        public String getDescription() {
            return "'Grace is for Church' You gain +2 strength, however, your dexterity is reduced by 1";
        }
    }

    public static class VeryTough extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setConScore(playerCharacter.getConScore() + 2);
            playerCharacter.setDexScore(playerCharacter.getDexScore() - 1);
        }

        public String getDescription() {
            return "Moons will be moved before you are, gain +2 Constitution at the cost of -1 Dexterity";
        }

    }

    public static class HiGAdapted extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setPowerSave(playerCharacter.getPowerSave() + 2);
            playerCharacter.setToughnessSave(playerCharacter.getToughnessSave() + 2);
            playerCharacter.setReflexSave(playerCharacter.getReflexSave() - 2);
        }

        public String getDescription() {
            return "'I've Fallen, and i can't get up!' Is considered one of the shortest, tragic stories where you grew up" +
                    "You gain +2 to your power and toughness saves, at the cost of -2 to your reflex saves. ";
        }

    }

    public static class Survival extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.SURVIVAL,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.SURVIVAL) + 1);
        }

        public String getDescription() {
            return "In Space, it's hard to find Bush, but you're excellent at bushcraft anyway, gain +1 Survival.";
        }
    }

    public static class Stalker extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.getSkills().setSkillValue(Skills.Skill.STEALTH,
                    playerCharacter.getSkills().getSkillValue(Skills.Skill.STEALTH) + 1);
        }

        public String getDescription() {
            return "'We Have more ... subtle methods' You gain +1 to your Stealth Skill.";
        }
    }

    public static class Flight extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            // This method can be used to apply the flight effect, such as modifying movement speed.
            playerCharacter.setCanFly(true);  // Example method to indicate flight capability.
            playerCharacter.setSpeed(playerCharacter.getSpeed() * 2);  // Double the ground speed.
        }

        public String getDescription() {
            return "If God intended Man to fly, he would've made them your species, you gain flight, and double your movement speed";
            //Find a way to balance this so gasbag isn't objectively shittier
        }
    }

    public static class Mindful extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setLogicSave(playerCharacter.getLogicSave() + 1);
            playerCharacter.setWillSave(playerCharacter.getWillSave() + 1);
        }

        public String getDescription() {
            return "Shields can take a hit, Guns can dish them out, only a Brain can build them, you gain +1 to your logic and will saves";
        }
    }
    public static class Thoughtful extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setIntScore(playerCharacter.getIntScore() + 1);
            playerCharacter.setIntMod(playerCharacter.getModifier(playerCharacter.getIntScore())); // Update modifier
        }

        public String getDescription() {
            return "You're clever enough to disregard the absurdity of reducing intelligence to a arbitrary statistic, that being said, you gain +1 to your intelligence score";
        }
    }
    public static class Beautiful extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setComScore(playerCharacter.getComScore() + 1);
        }

        public String getDescription() {
            return "You cause more thirst than a drought, gain +1 Comeliness";
        }
    }

    public static class Charming extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setChaScore(playerCharacter.getChaScore() + 1);
        }

        public String getDescription() {
            return "You are the difference between flirting and harassment, Gain +1 Charisma ";
        }
    }
    public static class Gregarious extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setCharmSave(playerCharacter.getCharmSave() + 2);
        }

        public String getDescription() {
            return "You've got the gift of the gab, and are quite generous, gain +2 to charm saves";
        }
    }

    public static class Sexy extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setLooksSave(playerCharacter.getLooksSave() + 1);
            playerCharacter.setCharmSave(playerCharacter.getCharmSave() + 1);
        }

        public String getDescription() {
            return "'WOWEEEEE HUMMMANHUMMANAN BOING AWOOGA' Gain +1 to Looks and Charm saves.";
        }
    }
    public static class Flexible extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.setReflexSave(playerCharacter.getReflexSave() + 1);
            playerCharacter.setToughnessSave(playerCharacter.getToughnessSave() + 1);
        }

        public String getDescription() {
            return "You hold the Osinium medal in Gymanastics, gain +1 to to Refelex and Toughness saves.";
        }
    }
    public static class Boiling extends RaceTrait {
        @Override
        public void applyEffect(PlayerCharacter playerCharacter) {
            playerCharacter.addResistance(DamageType.HEAT);
            playerCharacter.addVulnerability(DamageType.COLD);
        }

        public String getDescription() {
            return "Some like it hot, you like it boiling, gain resistance to heat damage and a vulnerability to cold.";
        }
    }






    // Add other traits as needed
}