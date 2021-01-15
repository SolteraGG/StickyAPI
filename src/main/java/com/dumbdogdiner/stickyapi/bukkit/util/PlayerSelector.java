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
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

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
     * <p>
     * Get the offset between two {@link org.bukkit.Location} objects
     * in a 3d environment using {@link Vector} objects.
     * </p>
     *
     * @param pointA first location vector
     * @param pointB second location vector
     * @return the offset (distance) between the given locations
     */
    public static double offset(Vector pointA, Vector pointB) {
        return pointA.clone().subtract(pointB).length();
    }

    /**
     * <p>
     * This Predicate will return true if the player is not in gamemode spectator
     * </p>
     */
    public static Predicate<Player> NOT_SPECTATING = player -> player.getGameMode() != GameMode.SPECTATOR;

    /**
     * <p>
     * This Predicate will return true if the player is in gamemode survival
     * </p>
     */
    public static Predicate<Player> IN_SURVIVAL = player -> player.getGameMode() == GameMode.SURVIVAL;

    /**
     * <p>
     * This Predicate will return true if the player is in gamemode creative
     * </p>
     */
    public static Predicate<Player> IN_CREATIVE = player -> player.getGameMode() == GameMode.CREATIVE;

    /**
     * <p>
     * This Predicate will return true if the player is in gamemode adventure
     * </p>
     */
    public static Predicate<Player> IN_ADVENTURE_MODE = player -> player.getGameMode() == GameMode.ADVENTURE;

    /**
     * <p>
     * This Predicate will return true if the player is in gamemode spectator
     * </p>
     */
    public static Predicate<Player> IN_SPECTATOR = player -> player.getGameMode() == GameMode.SPECTATOR;

    /**
     * <p>
     * This Predicate will return true if the player is an operator
     * </p>
     */
    public static Predicate<Player> IS_OPERATOR = ServerOperator::isOp;

    /**
     * <p>
     * This Predicate will return true if the player is within the radius
     * of a given location
     * </p>
     */
    public static Predicate<Player> inRange(Location center, double radius) {
        return player -> offset(center.toVector(), player.getLocation().toVector()) <= radius;
    }

    /**
     * <p>
     * Get a random player matching a given condition
     * </p>
     *
     * @param condition the condition the player should match
     * @return random player matching the given condition
     */
    public static Player selectRandom(Predicate<Player> condition) {
        List<Player> list = selectPlayers(condition);
        return list.get(MathUtil.randomInt(list.size()));
    }

    /**
     * <p>
     * Get a all players matching a given condition
     * </p>
     *
     * @param condition the condition the player should match
     * @return list of players matching the given condition
     */
    public static List<Player> selectPlayers(Predicate<Player> condition) {
        return Bukkit.getOnlinePlayers().stream().filter(condition).collect(Collectors.toList());
    }

    /**
     * <p>
     * Get all players visible to another player
     * </p>
     *
     * @param sender the player that should be tested
     * @return list of players matching the given condition
     */
    public static List<Player> selectVisible(Player sender) {
        return Bukkit.getOnlinePlayers().stream().filter(sender::canSee).collect(Collectors.toList());
    }

    /**
     * <p>
     * Get all players with a specific permission
     * </p>
     *
     * @param permission the permission that should be matched
     * @return list of players matching the given condition
     */
    public static List<Player> selectWithPermission(String permission) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).collect(Collectors.toList());
    }

}
