/**
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

import java.util.ArrayList;

/**
 * This class is for creating new slots in an inventory GUI
*/
public class ClickableSlot {

    @Getter
    public ItemStack item;
    @Getter
    public int slot;

    /**
     * Creates a ClickableSlot object that should be used
     * to register interactive slots in a {@link GUI} object
     * @param pMaterial The Material for this slot item
     * @param pName The Displayname for this slot item
     * @param pAmount Amount of items
     * @param pSlot The slot which should be used for this item
     * @param lore (Optional) Add lore to this item
     */
    public ClickableSlot(@NotNull Material pMaterial, @NotNull Integer pAmount, String pName, @NotNull Integer pSlot, String... lore) {
        this.item = new ItemStack(pMaterial, pAmount);

        ItemMeta isM = item.getItemMeta();

        if(pName != null) {
            isM.setDisplayName(ChatColor.translateAlternateColorCodes('&', pName));
        }

        ArrayList<String> metaLore = new ArrayList<>();

        for (String loreComments : lore) {
            loreComments = ChatColor.translateAlternateColorCodes('&', loreComments);
            metaLore.add(loreComments);
        }

        isM.setLore(metaLore);
        item.setItemMeta(isM);

        this.slot = pSlot;
    }

    /**
     * Creates a ClickableSlot object that should be used
     * to register interactive slots in a {@link GUI} object
     * @param item A pre-configured {@link org.bukkit.inventory.ItemStack} object
     * @param pSlot The slot which should be used for this item
     */
    public ClickableSlot(@NotNull ItemStack item, int pSlot) {
        this.item = item;
        this.slot = pSlot;
    }

    /**
     * Executes a specific task async. Typically used
     * within {@link org.bukkit.event.inventory.InventoryClickEvent}
     * @param task The async task that should be executed
     */
    public void execute(@NotNull Runnable task) {
        new Thread(task).start();
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
