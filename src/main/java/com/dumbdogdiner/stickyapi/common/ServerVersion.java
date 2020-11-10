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

package com.dumbdogdiner.stickyapi.common;

/**
 * Utility class for fetching version data.
 */
public final class ServerVersion {
    public enum ServerType {
        BUKKIT, SPIGOT, PAPER, BUNGEE, WATERFALL
    }

    /**
     * Retrieve the type of server this method was called by.
     * @return The type of the server running when this method is evaluated.
     */
    public static ServerType getServerType() {
        if (isBukkit()) {
            if (isPaper()) {
                return ServerType.PAPER;
            }
            if (isSpigot()) {
                return ServerType.SPIGOT;
            }
            return ServerType.BUKKIT;
        }
       
        if (isWaterfall()) {
            return ServerType.WATERFALL;
        }

        return ServerType.BUNGEE;
    }

    /**
     * Returns true if the server is running paper.
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
     * @return Whether or not the server is running spigot
     */
    public static boolean isSpigot() {
         try {
            return Class.forName("org.spigotmc.CustomTimingsHandler") != null || Class.forName("org.spigotmc.SpigotConfig") != null;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns true if the server is running bukkit.
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
     * Get the current version of bukkit. This method is valid for both Bukkit, Spigot, and Paper.
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
     * @return The current version of bungee
     */
    public static String getBungeeVersion() {
        try {
            return net.md_5.bungee.api.ProxyServer.getInstance().getVersion();
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
}
