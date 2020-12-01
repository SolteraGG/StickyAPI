/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import javax.annotation.Nullable;

import com.destroystokyo.paper.Title;

import com.dumbdogdiner.stickyapi.common.translation.Translation;
import com.dumbdogdiner.stickyapi.common.util.ReflectionUtil;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.TextComponent;

public class ServerUtil {
    private ServerUtil() {}

    // These are here so we don't have to re-declare them later
    private static Object minecraftServer;
    private static double[] recentTps;

    /**
     * Get the server's TPS over the last 15 minutes (1m, 5m, 15m)
     * 
     * @return {@link java.util.ArrayList}
     */
    public static double[] getRecentTps() {
        if (minecraftServer == null) {
                minecraftServer = ReflectionUtil.getProtectedValue(Bukkit.getServer(), "console");
        }
        if (recentTps == null) {
            recentTps = ReflectionUtil.getProtectedValue(minecraftServer.getClass().getSuperclass(), "recentTps");
        }
            
        return recentTps;
    }

    /**
     * Broadcast a colored and formatted message to a {@link Server}.
     * 
     * @param message The message to broadcast
     * @param args    The format arguments, if any
     */
    public static void broadcastMessage(@NotNull String message, @Nullable String... args) {
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
    public static void broadcastTitle(@NotNull Title title) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendTitle(title);
        }
    }
}
