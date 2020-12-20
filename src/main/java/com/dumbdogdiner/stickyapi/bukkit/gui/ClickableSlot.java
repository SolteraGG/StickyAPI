/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * This class is for creating new slots in an inventory GUI
*/
public class ClickableSlot {
    @Getter
    public ItemStack item;
    @Getter
    public final int x;
    @Getter
    public final int y;

    /**
     * Creates a ClickableSlot object that should be used
     * to register interactive slots in a {@link GUI} object
     * @param material The Material for this slot item
     * @param amount Amount of items
     * @param name The display name for this slot item
     * @param x The x position of the item
     * @param y The y position of the item
     * @param lore (Optional) Add lore to this item
     */
    public ClickableSlot(@NotNull Material material, int amount, @Nullable String name, int x, int y, String... lore) {
        this(makeItem(material, amount, name, lore), x, y);
    }

    private static ItemStack makeItem(Material material, int amount, String name, String[] lore) {
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        ArrayList<String> metaLore = new ArrayList<>();

        for (String loreComments : lore) {
            metaLore.add(ChatColor.translateAlternateColorCodes('&', loreComments));
        }

        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a ClickableSlot object that should be used
     * to register interactive slots in a {@link GUI} object
     * @param item A pre-configured {@link org.bukkit.inventory.ItemStack} object
     * @param x The x position of the item
     * @param y The y position of the item
     */
    public ClickableSlot(@NotNull ItemStack item, int x, int y) {
        this.item = item;
        this.x = x;
        this.y = y;
    }

    public ItemMeta getMeta() {
        return item.getItemMeta();
    }

    public void setName(@NotNull String s) {
        ItemMeta isM = this.item.getItemMeta();
        isM.setDisplayName(s);
        item.setItemMeta(isM);
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }
}
