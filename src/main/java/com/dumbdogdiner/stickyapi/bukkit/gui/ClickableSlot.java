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

import java.util.ArrayList;

/**
 * This class is for creating new slots in an inventory GUI
*/
public class ClickableSlot {

    @Getter public ItemStack item;
    @Getter public int slot;

    public ClickableSlot(Material pMaterial, String pName, int pAmount, int pSlot, String... lore) {
        this.item = new ItemStack(pMaterial, pAmount);

        ItemMeta isM = item.getItemMeta();
        isM.setDisplayName(ChatColor.translateAlternateColorCodes('&', pName));
        ArrayList<String> metaLore = new ArrayList<>();

        for (String loreComments : lore) {
            loreComments = ChatColor.translateAlternateColorCodes('&', loreComments);
            metaLore.add(loreComments);
        }

        isM.setLore(metaLore);
        item.setItemMeta(isM);

        this.slot = pSlot;
    }

    public ClickableSlot(ItemStack item, int pSlot) {
        this.item = item;

        this.slot = pSlot;
    }

    public void execute(Runnable r) {
        new Thread(r).start();
    }

    public ItemMeta getMeta() {
        return item.getItemMeta();
    }

    public void setName(String s) {
        ItemMeta isM = this.item.getItemMeta();
        isM.setDisplayName(s);
        item.setItemMeta(isM);
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }
}
