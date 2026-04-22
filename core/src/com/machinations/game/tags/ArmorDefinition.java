package com.machinations.game.tags;

import java.util.List;
import com.machinations.game.Dice;

import java.util.ArrayList;
import java.util.Collections;

public class ArmorDefinition {

    private final String id;
    private final String name;
    private final String description;

    private final Dice armourDie;
    private final int rangedDefenceBonus;
    private final int closeDefenceBonus;
    private final int defencePenalty;
    private final int costGp;

    private final ArrayList<ArmorTag> tags;

    public ArmorDefinition(String id,
                           String name,
                           String description,
                           Dice armourDie,
                           int rangedDefenceBonus,
                           int closeDefenceBonus,
                           int defencePenalty,
                           int costGp,
                           List<ArmorTag> tags) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.armourDie = armourDie;
        this.rangedDefenceBonus = rangedDefenceBonus;
        this.closeDefenceBonus = closeDefenceBonus;
        this.defencePenalty = defencePenalty;
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

    public Dice getArmourDie() {
        return armourDie;
    }

    public int getRangedDefenceBonus() {
        return rangedDefenceBonus;
    }

    public int getCloseDefenceBonus() {
        return closeDefenceBonus;
    }

    public int getDefencePenalty() {
        return defencePenalty;
    }

    public int getCostGp() {
        return costGp;
    }

    public List<ArmorTag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public boolean hasTag(ArmorTag tag) {
        return tags.contains(tag);
    }

    @Override
    public String toString() {
        return "ArmorDefinition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", armourDie=" + armourDie +
                ", rangedDefenceBonus=" + rangedDefenceBonus +
                ", closeDefenceBonus=" + closeDefenceBonus +
                ", defencePenalty=" + defencePenalty +
                ", costGp=" + costGp +
                ", tags=" + tags +
                '}';
    }
}

