package com.machinations.game;

public class Inventory {
    private final ItemRegistry itemRegistry;
    private final ItemEffectRegistry effectRegistry;

    private final ItemStack[] slots;

    public Inventory(ItemRegistry itemRegistry, ItemEffectRegistry effectRegistry, int capacity) {
        this.itemRegistry = itemRegistry;
        this.effectRegistry = effectRegistry;
        this.slots = new ItemStack[capacity];
    }

    public ItemStack[] getSlots() {
        return slots;
    }

    public boolean add(String itemId, int amount) {
        ItemDefinition def = itemRegistry.get(itemId);
        if (def == null || amount <= 0) return false;

        // Try stacking first
        if (def.stackable) {
            for (ItemStack s : slots) {
                if (s != null && s.itemId.equals(itemId) && s.quantity < def.maxStack) {
                    int canAdd = Math.min(amount, def.maxStack - s.quantity);
                    s.quantity += canAdd;
                    amount -= canAdd;
                    if (amount == 0) return true;
                }
            }
        }

        // Add into empty slots
        while (amount > 0) {
            int empty = firstEmptySlot();
            if (empty == -1) return false;

            int toPut = def.stackable ? Math.min(amount, def.maxStack) : 1;
            slots[empty] = new ItemStack(itemId, toPut);
            amount -= toPut;
        }

        return true;
    }

    public boolean useSlot(int slotIndex, PlayerCharacter pc, Log log) {
        if (slotIndex < 0 || slotIndex >= slots.length) return false;
        ItemStack stack = slots[slotIndex];
        if (stack == null) return false;

        ItemDefinition def = itemRegistry.get(stack.itemId);
        if (def == null) return false;

        // Equip toggle
        if (def.equipable) {
            boolean nowEquipped = !stack.equipped;
            stack.equipped = nowEquipped;

            if (def.kind == ItemKind.ARMOR) {
                if (nowEquipped) {
                   /// pc.equipArmor(def.armorType);
                    if (log != null) log.addEntry("Equipped: " + def.name);
                } else {
                 ///   pc.unequipArmor();
                    if (log != null) log.addEntry("Unequipped: " + def.name);
                }
            }

            return true;
        }

        // Normal "use" effect
        boolean success = effectRegistry.apply(def.onUseEffect, pc, log);
        if (success && def.consumable) {
            stack.quantity -= 1;
            if (stack.quantity <= 0) slots[slotIndex] = null;
        }

        return success;
    }

    private int firstEmptySlot() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) return i;
        }
        return -1;
    }
}