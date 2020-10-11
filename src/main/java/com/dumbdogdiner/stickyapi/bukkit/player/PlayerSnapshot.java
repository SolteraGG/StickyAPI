/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
