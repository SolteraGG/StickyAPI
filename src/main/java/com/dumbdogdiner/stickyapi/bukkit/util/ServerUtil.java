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

import com.dumbdogdiner.stickyapi.common.translation.Translation;

import org.bukkit.Server;

import net.md_5.bungee.api.chat.TextComponent;

public class ServerUtil {

    // These are here so we don't have to re-declare them later
    private static Object minecraftServer;
    private static Field recentTps; 
    
    /**
     * Get the server's TPS in the last 15 minutes (1m, 5m, 15m)
     * @param server The {@link org.bukkit.Server} to get the TPS of
     * @return {@link java.lang.Double}
     */
    public static double[] getRecentTps(Server server) {        
        try {
            if (minecraftServer == null) {
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return new double[] {0, 0, 0}; // If there's an issue, let's make it known.
    }

    /**
     * Broadcast a colored and formatted message to a {@org.bukkit.Server}.
     * @param server The {@link org.bukkit.Server} to broadcast to
     * @param message The message to broadcast
     * @param args The format arguments, if any
     */
    public static void broadcastMessage(Server server, String message, String... args) {
        server.broadcast(new TextComponent(Translation.translateColors("&", String.format(message, (Object)args))));
    }
    
}
