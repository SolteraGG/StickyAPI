/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.player;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;

public class PlayerSnapshot implements Serializable {

    private final Location location;
    private final ItemStack[] armor;
    private final ItemStack[] items;
    private final double health;
    private final int foodLevel;
    private final float saturation;
    private final float exhaustion;
    private final float exp;
    private final Vector velocity;
    @Getter
    private final Instant savedAt;

    /**
     * Create a new {@link PlayerSnapshot}.
     * 
     * A {@link PlayerSnapshot} is an object to store the player's current state,
     * such as their inventory, health, experience, etc.
     * <p>
     * This object can be used where the player inventory needs to be changed but
     * restored at a later date
     * 
     * @param player the Player to snapshot
     */
    public PlayerSnapshot(@NotNull Player player) {
        this.savedAt = Instant.now();
        this.location = player.getLocation();
        this.armor = player.getInventory().getArmorContents();
        this.items = player.getInventory().getContents();
        this.health = player.getHealth();
        this.foodLevel = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.exhaustion = player.getExhaustion();
        this.exp = player.getExp();
        this.velocity = player.getVelocity();
    }

    /**
     * Apply this {@link PlayerSnapshot} to a {@link Player}.
     * 
     * @param player The player to apply this {@link PlayerSnapshot} to
     */
    public void apply(@NotNull Player player) {
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
