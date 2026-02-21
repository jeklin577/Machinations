package com.machinations.game;

public class ItemStack {
    public final String itemId;
    public int quantity;
    public boolean equipped;

    public ItemStack(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.equipped = false;
    }
}