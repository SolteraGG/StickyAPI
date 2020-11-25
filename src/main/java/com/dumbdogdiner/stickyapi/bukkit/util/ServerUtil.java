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

package com.dumbdogdiner.stickyapi.bukkit.util;

import java.lang.reflect.Field;

import com.destroystokyo.paper.Title;
import com.dumbdogdiner.stickyapi.common.translation.Translation;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

public class ServerUtil {

    // These are here so we don't have to re-declare them later
    private static Object minecraftServer;
    private static Field recentTps;

    /**
     * Get the server's TPS over the last 15 minutes (1m, 5m, 15m)
     * 
     * @return {@link java.util.ArrayList}
     */
    public static double[] getRecentTps() {
        try {
            if (minecraftServer == null) {
                Field consoleField = Bukkit.getServer().getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                minecraftServer = consoleField.get(Bukkit.getServer());
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return new double[] { 0, 0, 0 }; // If there's an issue, let's make it known.
    }

    /**
     * Broadcast a colored and formatted message to a {@link Server}.
     * 
     * @param message The message to broadcast
     * @param args    The format arguments, if any
     */
    public static void broadcastMessage(String message, String... args) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(
                    new TextComponent(Translation.translateColors("&", String.format(message, (Object) args))));
        }
    }

    /**
     * Send a title to all online players
     * 
     * @param title The {@link com.destroystokyo.paper.Title} to send
     */
    public static void broadcastTitle(Title title) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendTitle(title);
        }
    }
}
