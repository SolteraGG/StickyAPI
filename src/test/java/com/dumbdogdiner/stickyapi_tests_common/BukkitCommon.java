/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi_tests_common;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class BukkitCommon {
    public static void getMockedBukkit(Consumer<?> func) {
        // Mock the Bukkit server
        Server server = mock(Server.class);

        // Mock the Bukkit player
        Player p1 = mock(Player.class);
        //doNothing().when(p1).sendMessage(any(TextComponent.class));

        // Create a new online player Collection
        ArrayList c2 = new ArrayList();
        c2.add(p1);

        try (MockedStatic<Bukkit> mocked = mockStatic(Bukkit.class)) {
            // When Bukkit.getServer().getOnlinePlayers() is called return the new collection

            mocked.when(Bukkit::getServer).thenReturn(server);

            when(server.getOnlinePlayers()).thenReturn(c2);

            func.accept(null);

            mocked.verify(Bukkit::getServer);
            verify(server).getOnlinePlayers();
        }
    }
}
