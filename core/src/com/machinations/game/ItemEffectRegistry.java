package com.machinations.game;

public class ItemEffectRegistry {

    /**
     * Return true if the use “succeeds” (so consumables can be consumed).
     */

    //So, this defines effects, so if we have a drug that deals a signficant ammount of max_hp damage for stats
    //(Steroids are for sure on theme), we'd set maxhealth to getmaxHP-x, then increase the relevant stat
    //Doing this, we can define a few bits, so for instance, so rather than individually typing common things, like "deal a flat 1 damage"
    //We can reuse them
    public boolean apply(ItemEffectId effectId, PlayerCharacter pc, Log log) {
        switch (effectId) {
            case HEAL_5:
                pc.setHealth(pc.getHealth() + 5);
                if (log != null) log.addEntry(pc.getName() + " healed 5 HP.");
                return true;

            case HEAL_10:
                pc.setHealth(pc.getHealth() + 10);
                if (log != null) log.addEntry(pc.getName() + " healed 10 HP.");
                return true;

            case NONE:
            default:
                return true;
        }
    }
}