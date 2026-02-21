package com.machinations.game;

public class ItemDefinition {

    public final String id;
    public final String name;
    public final String description;

    public final ItemKind kind;

    // stacking
    public final boolean stackable;
    public final int maxStack;

    // use/equip behaviour
    public final boolean consumable;     // if true: quantity decreases on successful use
    public final boolean equipable;      // if true: "use" toggles equip state

    // effect
    public final ItemEffectId onUseEffect;

    // armor fields (only meaningful when kind == ARMOR)
    public final String armorType;       // reuses your existing strings: "Light", "Heavy", etc.

    public ItemDefinition(
            String id,
            String name,
            String description,
            ItemKind kind,
            boolean stackable,
            int maxStack,
            boolean consumable,
            boolean equipable,
            ItemEffectId onUseEffect,
            String armorType
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.kind = kind;
        this.stackable = stackable;
        this.maxStack = maxStack;
        this.consumable = consumable;
        this.equipable = equipable;
        this.onUseEffect = onUseEffect;
        this.armorType = armorType;
    }


}
