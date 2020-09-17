package com.ristexsoftware.koffee.bukkit.GUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;

public class ClickableSlot {
    
    @Getter
    private ItemStack item;

    @Getter
    private int slot;

    @Getter
    private ItemMeta meta = item.getItemMeta();

    public ClickableSlot(Material pMaterial, String pName, int pAmount, int pSlot) {
        this.item = new ItemStack(pMaterial, pAmount);

        ItemMeta isM = item.getItemMeta();
        isM.setDisplayName(pName);
        item.setItemMeta(isM);

        this.slot = pSlot;
    }

    public void execute(Runnable r) {
        new Thread(r).start();
    }

    public void setName(String s) {
        ItemMeta isM = this.item.getItemMeta();
        isM.setDisplayName(s);
        item.setItemMeta(isM);
    }

    public String GetName() {
        return item.getItemMeta().getDisplayName();
    }
}