package com.ristexsoftware.koffee.bukkit.GUI;

import java.util.HashMap;
import java.util.Map;

import com.ristexsoftware.koffee.arguments.Arguments;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import lombok.Getter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public abstract class GUI implements Listener {

    @Getter
    private Inventory inventory;
    private int s;

    @Getter
    private Plugin plugin;
    private Map<Integer, ClickableSlot> clickableMap;

    protected GUI(int pSize, Plugin pPlugin) {
        this.s = pSize;
        this.plugin = pPlugin;

        this.clickableMap = new HashMap<Integer, ClickableSlot>();

    }

    protected void registerSlot(ItemStack pItem, int pSlot) {
        if(pItem == null) 
            return;
        inventory.setItem(pSlot, pItem);
    }

    protected void registerClickable(ClickableSlot cs) {
        if(cs.getItem() == null || inventory.getItem(cs.getSlot()) != null) return;
        clickableMap.put(cs.getSlot(), cs);
        inventory.setItem(cs.getSlot(), cs.getItem());
    }

    protected ClickableSlot getClickable(int slot) {
        return clickableMap.get(slot);
    }

    protected abstract void buildGUI(Player player, String[] args, Arguments a);
    
    protected boolean isValidSlot(int pSlot) {
        return inventory.getItem(pSlot) != null;
    }

    protected boolean isClickable(int pSlot) {
        return clickableMap.containsKey(pSlot);
    }

    @EventHandler
    protected abstract void onSlotClick(InventoryClickEvent e);

    protected Inventory makeInventory(String pTitle) {
        return this.inventory = Bukkit.createInventory(null, s, pTitle);
    }
}