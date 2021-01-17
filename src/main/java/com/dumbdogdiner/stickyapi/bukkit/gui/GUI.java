/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Represents an inventory GUI. Item stacks can be placed at locations in the
 * GUI, and can be assigned tags and callbacks. A single GUI can be shared by
 * multiple players, as updates will appear for all players viewing the GUI.
 */
public class GUI {
    /** The number of rows in the inventory */
    @Getter
    private final int rows;

    /** The plugin responsible for this GUI */
    @Getter
    private final @NotNull Plugin plugin;

    /** The name of this inventory */
    @Getter
    private @NotNull String name;

    /**
     * The inventory used by this GUI. The inventory is replaced each time the name
     * of this GUI changes
     */
    @Getter
    private @NotNull Inventory inventory;

    private final Map<Integer, GUISlot> slots = new HashMap<>();

    public static final int ROW_LENGTH = 9;

    /**
     * Create a GUI with the given number of rows.
     * 
     * @param rows   The number of rows in the inventory, must be between 1 and 6
     * @param name   The name of the GUI, displayed as the inventory's name.
     * @param plugin The plugin responsible for this GUI
     */
    public GUI(int rows, @NotNull String name, @NotNull Plugin plugin) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("GUI cannot have " + rows + " rows");
        }

        this.rows = rows;
        this.name = name;
        this.plugin = plugin;

        inventory = Bukkit.createInventory(null, ROW_LENGTH * rows, name);
    }

    private int makeIndex(int x, int y) {
        if (x < 0 || x >= ROW_LENGTH) {
            throw new IllegalArgumentException("Invalid GUI row " + x);
        }

        if (y < 0 || y >= rows) {
            throw new IllegalArgumentException("Invalid GUI column " + y);
        }

        return x + ROW_LENGTH * y;
    }

    /**
     * Adds an item stack to the GUI.
     * 
     * @param x    The x position of the item stack, must be between 0 and 8
     * @param y    The y position of the item stack, must be between 0 and
     *             {@link GUI#getRows()} - 1
     * @param slot The {@link GUISlot} to put in this location.
     */
    public void addSlot(int x, int y, @NotNull GUISlot slot) {
        int index = makeIndex(x, y);
        slots.put(index, slot);
        inventory.setItem(index, slot.getItemStack());
    }

    /**
     * Adds an item stack to the GUI.
     * 
     * @param x      The x position of the item stack, must be between 0 and 8
     * @param y      The y position of the item stack, must be between 0 and
     *               {@link GUI#getRows()} - 1
     * @param stack  The item stack to put at this location
     * @param tag    An optional tag that be used to identify the item in
     *               {@link GUI#onInventoryClick(InventoryClickEvent, String)}
     * @param action An optional procedure to run when this item is clicked,
     *               receives the {@link InventoryClickEvent}
     */
    public void addSlot(int x, int y, @NotNull ItemStack stack, @Nullable String tag,
            @Nullable BiConsumer<InventoryClickEvent, GUI> action) {
        addSlot(x, y, new GUISlot(stack, tag, action));
    }

    public void addSlot(int x, int y, @NotNull ItemStack stack, @Nullable String tag) {
        addSlot(x, y, stack, tag, null);
    }

    public void addSlot(int x, int y, @NotNull ItemStack stack, @Nullable BiConsumer<InventoryClickEvent, GUI> action) {
        addSlot(x, y, stack, null, action);
    }

    public void addSlot(int x, int y, @NotNull ItemStack stack) {
        addSlot(x, y, stack, null, null);
    }

    /**
     * Add a {@link ClickableSlot} to the GUI.
     * 
     * @param cs     The clickable slot to put into the GUI
     * @param tag    An optional tag that be used to identify the item in
     *               {@link GUI#onInventoryClick(InventoryClickEvent, String)}
     * @param action An optional procedure to run when this item is clicked,
     *               receives the {@link InventoryClickEvent}
     */
    public void addSlot(@NotNull ClickableSlot cs, @Nullable String tag,
            @Nullable BiConsumer<InventoryClickEvent, GUI> action) {
        addSlot(cs.getX(), cs.getY(), cs.getItem(), tag, action);
    }

    public void addSlot(ClickableSlot cs, String tag) {
        addSlot(cs, tag, null);
    }

    public void addSlot(ClickableSlot cs, BiConsumer<InventoryClickEvent, GUI> action) {
        addSlot(cs, null, action);
    }

    public void addSlot(ClickableSlot cs) {
        addSlot(cs, null, null);
    }

    /**
     * Removes a slot in the GUI.
     * 
     * @param x The x position of the slot, must be between 0 and 8
     * @param y The y position of the slot, must be between 0 and
     *          {@link GUI#getRows()} - 1
     */
    public void removeSlot(int x, int y) {
        int index = makeIndex(x, y);
        slots.remove(index);
        inventory.setItem(index, null);
    }

    /**
     * Gets a slot in the GUI.
     * 
     * @param x The x position of the slot, must be between 0 and 8
     * @param y The y position of the slot, must be between 0 and
     *          {@link GUI#getRows()} - 1
     * @return The {@link GUISlot} at this location, or null if none exists
     */
    public @Nullable GUISlot getSlot(int x, int y) {
        return slots.get(makeIndex(x, y));
    }

    /**
     * Open this GUI for a player.
     * 
     * @param player The player who will see this GUI
     */
    public void open(Player player) {
        var self = this;
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInventoryOpen(InventoryOpenEvent event) {
                if (event.getInventory() == inventory) {
                    self.onInventoryOpen(event);
                }
            }

            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getClickedInventory() == inventory) {
                    event.setCancelled(true);
                    // only allow basic clicks, other clicks might allow players to smuggle gui
                    // items out of the inv
                    switch (event.getClick()) {
                        case LEFT:
                        case RIGHT:
                            break;
                        default:
                            return;
                    }

                    var slot = slots.get(event.getSlot());
                    String tag = null;
                    if (slot != null) {
                        tag = slot.getTag();
                        var action = slot.getAction();
                        if (action != null) {
                            action.accept(event, self);
                        }
                    }
                    self.onInventoryClick(event, tag);
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getInventory() == inventory) {
                    self.onInventoryClose(event);
                    InventoryOpenEvent.getHandlerList().unregister(this);
                    InventoryClickEvent.getHandlerList().unregister(this);
                    InventoryCloseEvent.getHandlerList().unregister(this);
                    // if a player hotbars an item and closes the gui on the same tick, their client
                    // thinks they have
                    // the item when they really do not, and if creative, the server believes them.
                    // this prevents that
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        ((Player) event.getPlayer()).updateInventory();
                    });
                }
            }
        }, plugin);
        player.openInventory(inventory);
    }

    /**
     * Fired when an inventory created by this class is opened.
     * 
     * @param event The event
     */
    protected void onInventoryOpen(@NotNull InventoryOpenEvent event) {
    }

    /**
     * Fired when a slot is clicked in an inventory created by this class. The event
     * will automatically prevent players from taking items out of the inventory,
     * and it is recommended not to change this behavior.
     * 
     * @param event The event
     * @param tag   The tag associated with the item, if any
     */
    protected void onInventoryClick(@NotNull InventoryClickEvent event, @Nullable String tag) {
    }

    /**
     * Fired when an inventory created by this class is closed.
     * 
     * @param event The event
     */
    protected void onInventoryClose(@NotNull InventoryCloseEvent event) {
    }

    // custom setter for inventory name, as inv name normally cannot be changed
    public void setName(@NotNull String name) {
        var viewers = new ArrayList<>(inventory.getViewers());
        var contents = inventory.getContents();
        inventory = Bukkit.createInventory(null, ROW_LENGTH * rows, name);
        inventory.setContents(contents);
        for (var viewer : viewers) {
            viewer.openInventory(inventory);
        }
        this.name = name;
    }

    /**
     * Create a copy of this GUI.
     * 
     * @return A copy of this GUI with no viewers.
     */
    public @NotNull GUI duplicate() {
        var gui = new GUI(rows, name, plugin);
        for (var slot : slots.entrySet()) {
            int index = slot.getKey();
            gui.addSlot(index % 9, index / 9, slot.getValue().duplicate());
        }
        return gui;
    }
}
