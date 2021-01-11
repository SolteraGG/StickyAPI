/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import com.dumbdogdiner.stickyapi.common.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * Util class for selecting players fulfilling a given criterion
 * </p>
 */
public class PlayerSelector {

    /**
     * This Predicate will return true if the player is not in gamemode spectator
     */
    public static Predicate<Player> NOT_SPECTATING = player -> player.getGameMode() != GameMode.SPECTATOR;

    /**
     * This Predicate will return true if the player is in gamemode survival
     */
    public static Predicate<Player> IN_SURVIVAL = player -> player.getGameMode() == GameMode.SURVIVAL;

    /**
     * This Predicate will return true if the player is an operator
     */
    public static Predicate<Player> IS_OPERATOR = ServerOperator::isOp;

    /**
     * This Predicate will return true if the player is within the radius
     * of a given location
     */
    public static Predicate<Player> inRange(Location center, double radius) {
        return player -> MathUtil.offset(center.toVector(), player.getLocation().toVector()) <= radius;
    }

    /**
     * Get a random player matching a given condition
     *
     * @param condition the condition the player should match
     * @return random player matching the given condition
     */
    public static Player selectRandom(Predicate<Player> condition) {
        List<Player> list = selectPlayers(condition);
        return list.get(MathUtil.rInt(list.size()));
    }

    /**
     * Get a all players matching a given condition
     *
     * @param condition the condition the player should match
     * @return list of players matching the given condition
     */
    public static List<Player> selectPlayers(Predicate<Player> condition) {
        return Bukkit.getOnlinePlayers().stream().filter(condition).collect(Collectors.toList());
    }

    /**
     * Get all players visible to another player
     *
     * @param sender the player that should be tested
     * @return list of players matching the given condition
     */
    public static List<Player> selectVisible(Player sender) {
        return Bukkit.getOnlinePlayers().stream().filter(sender::canSee).collect(Collectors.toList());
    }

    /**
     * Get all players with a specific permission
     *
     * @param permission the permission that should be matched
     * @return list of players matching the given condition
     */
    public static List<Player> selectWithPermission(String permission) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).collect(Collectors.toList());
    }

}
