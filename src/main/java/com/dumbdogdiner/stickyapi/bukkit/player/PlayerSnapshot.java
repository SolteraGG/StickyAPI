/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerSnapshot {

    private Location location;
    private ItemStack[] armor;
    private ItemStack[] items;
    private double health;
    private int foodLevel;
    private float saturation;
    private float exhaustion;
    private float exp;
    private Vector velocity;

    /**
     * Create a new {@link PlayerSnapshot}.
     * 
     * A {@link PlayerSnapshot} is an object to store the player's current state, such as their inventory, health, experience, etc.
     * <p>This object can be used where the player inventory needs to be changed but restored at a later date
     * @param player
     */
    public PlayerSnapshot(Player player) {
        location = player.getLocation();
        armor = player.getInventory().getArmorContents();
        items = player.getInventory().getContents();
        health = player.getHealth();
        foodLevel = player.getFoodLevel();
        saturation = player.getSaturation();
        exhaustion = player.getExhaustion();
        exp = player.getExp();
        velocity = player.getVelocity();

    }

    /**
     * Apply this {@link PlayerSnapshot} to a {@link Player}.
     * @param player The player to apply this {@link PlayerSnapshot} to
     */
    public void apply(Player player) {
        player.teleport(location);
        player.getInventory().setArmorContents(armor);
        player.getInventory().setContents(items);
        player.setHealth(health);
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        player.setExhaustion(exhaustion);
        player.setExp(exp);
        player.setVelocity(velocity);
    }
}
