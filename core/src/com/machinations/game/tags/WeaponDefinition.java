package com.machinations.game.tags;

import com.machinations.game.DamageType;
import com.machinations.game.Dice;
import com.machinations.game.tags.WeaponTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeaponDefinition {

    private final String id;
    private final String name;
    private final String description;

    private final boolean melee;
    private final Dice damageDie;
    private final DamageType damageType;

    private final int attackBonus;
    private final int rangeMeters;
    private final int costGp;

    private final ArrayList<WeaponTag> tags;

    public WeaponDefinition(String id,
                            String name,
                            String description,
                            boolean melee,
                            Dice damageDie,
                            DamageType damageType,
                            int attackBonus,
                            int rangeMeters,
                            int costGp,
                            List<WeaponTag> tags) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.melee = melee;
        this.damageDie = damageDie;
        this.damageType = damageType;
        this.attackBonus = attackBonus;
        this.rangeMeters = rangeMeters;
        this.costGp = costGp;
        this.tags = new ArrayList<>();

        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMelee() {
        return melee;
    }

    public Dice getDamageDie() {
        return damageDie;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getRangeMeters() {
        return rangeMeters;
    }

    public int getCostGp() {
        return costGp;
    }

    public List<WeaponTag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public boolean hasTag(WeaponTag tag) {
        return tags.contains(tag);
    }

    public boolean isRanged() {
        return !melee;
    }

    public static WeaponDefinition createMelee(String id,
                                               String name,
                                               String description,
                                               Dice damageDie,
                                               DamageType damageType,
                                               int attackBonus,
                                               int costGp,
                                               List<WeaponTag> tags) {

        return new WeaponDefinition(
                id,
                name,
                description,
                true,
                damageDie,
                damageType,
                attackBonus,
                0,
                0,
                tags
        );
    }

    public static WeaponDefinition createRanged(String id,
                                                String name,
                                                String description,
                                                Dice damageDie,
                                                DamageType damageType,
                                                int attackBonus,
                                                int rangeMeters,
                                                int ammoSave,
                                                int costGp,
                                                List<WeaponTag> tags) {

        return new WeaponDefinition(
                id,
                name,
                description,
                false,
                damageDie,
                damageType,
                attackBonus,
                rangeMeters,
                costGp,
                tags
        );
    }

    @Override
    public String toString() {
        return "WeaponDefinition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", melee=" + melee +
                ", damageDie=" + damageDie +
                ", damageType=" + damageType +
                ", attackBonus=" + attackBonus +
                ", rangeMeters=" + rangeMeters +
                ", costGp=" + costGp +
                ", tags=" + tags +
                '}';
    }
}

