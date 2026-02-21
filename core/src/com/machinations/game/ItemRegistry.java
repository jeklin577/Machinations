package com.machinations.game;

import java.util.Map;

public class ItemRegistry {
    private final Map<String, ItemDefinition> registry;

    public ItemRegistry() {
        registry = Map.of(
                "MEDKIT", new ItemDefinition(
                        "MEDKIT",
                        "Medkit",
                        "Restores a small amount of HP.",
                        ItemKind.CONSUMABLE,
                        true, 10,
                        true,  false,
                        ItemEffectId.HEAL_5,
                        null
                ),
                "ARMOR_LIGHT", new ItemDefinition(
                        "ARMOR_LIGHT",
                        "Light Armour",
                        "Basic protective gear.",
                        ItemKind.ARMOR,
                        false, 1,
                        false, true,
                        ItemEffectId.NONE,
                        "Light"
                )
        );
    }

    public ItemDefinition get(String id) {
        return registry.get(id);
    }
}