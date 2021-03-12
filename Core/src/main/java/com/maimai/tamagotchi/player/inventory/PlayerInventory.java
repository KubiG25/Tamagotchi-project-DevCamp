package com.maimai.tamagotchi.player.inventory;

import com.maimai.tamagotchi.inventory.Inventory;
import com.maimai.tamagotchi.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInventory implements Inventory {

    private final List<Item> items;

    public PlayerInventory() {
        this.items = new ArrayList<>();
    }

    @Override
    public Item getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public void setItem(int index, Item item) {
        this.items.set(index, item);
    }

    @Override
    public void addItems(Item... items) {
        this.items.addAll(Arrays.asList(items));
    }

}