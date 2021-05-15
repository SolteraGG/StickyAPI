/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common;

import lombok.experimental.UtilityClass;
import static com.dumbdogdiner.stickyapi.common.ServerVersion.ServerType.*;
/**
 * Utility class for fetching version data.
 */
@UtilityClass
public final class ServerVersion {
    public enum ServerType {
        BUKKIT, SPIGOT, PAPER, TUINITY, AIRPLANE, PURPUR, BUNGEE, WATERFALL
    }

    /**
     * Retrieve the type of server this method was called by.
     * 
     * @return The type of the server running when this method is evaluated.
     */
    public static ServerType getServerType() {
        if (isBukkit()) {
            if(isPurpur())
                return PURPUR;
            else if(isAirplane())
                return AIRPLANE;
            else if(isTuinity())
                return TUINITY;
            else if (isPaper())
                return PAPER;
            else if (isSpigot())
                return SPIGOT;
            else
                return BUKKIT;
        } else if(isBungee()) {
            if (isWaterfall())
                return WATERFALL;
            return BUNGEE;
        }

        throw new UnsupportedOperationException("Unknown server type, is this even a server??");
    }

    /**
     * Returns true if the server is running tuinity.
     *
     * @return Whether or not the server is running tuinity
     */
    public static boolean isTuinity() {
        try {
            return Class.forName("com.tuinity.tuinity.config.TuinityConfig") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running airplane.
     *
     * @return Whether or not the server is running airplane
     */
    public static boolean isAirplane() {
        try {
            return Class.forName("gg.airplane.AirplaneCommand") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running purpur.
     *
     * @return Whether or not the server is running purpur
     */
    public static boolean isPurpur() {
        try {
            return Class.forName("net.pl3x.purpur.PurpurVersionFetcher") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running paper.
     * 
     * @return Whether or not the server is running paper
     */
    public static boolean isPaper() {
        try {
            return Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running spigot.
     * 
     * @return Whether or not the server is running spigot
     */
    public static boolean isSpigot() {
        try {
            return Class.forName("org.spigotmc.CustomTimingsHandler") != null
                    || Class.forName("org.spigotmc.SpigotConfig") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running bukkit.
     * 
     * @return Whether or not the server is running bukkit
     */
    public static boolean isBukkit() {
        try {
            return Class.forName("org.bukkit.Server") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running waterfall.
     * 
     * @return Whether or not the server is running waterfall
     */
    public static boolean isWaterfall() {
        try {
            return Class.forName("io.github.waterfallmc.waterfall.QueryResult") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running bungee.
     * 
     * @return Whether or not the server is running bungeecord
     */
    public static boolean isBungee() {
        try {
            return Class.forName("net.md_5.bungee.api.ProxyServer") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Get the current version of bukkit. This method is valid for both Bukkit,
     * Spigot, and Paper.
     * 
     * @return The current version of bukkit
     */
    public static String getBukkitVersion() {
        try {
            return org.bukkit.Bukkit.getVersion();
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    /**
     * Get the current version of bungee.
     * 
     * @return The current version of bungee
     */
    public static String getBungeeVersion() {
        try {
            return net.md_5.bungee.api.ProxyServer.getInstance().getVersion();
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }
}
