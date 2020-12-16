/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

import com.destroystokyo.paper.Title;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ServerUtilTest {

    private void getMockedBukkit(Consumer<?> func) {
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

    @Test
    public void testBroadcastMessage() {
        getMockedBukkit(i -> {
            ServerUtil.broadcastMessage("hi");
        });
    }

    @Test
    public void testBroadcastTitle() {
        getMockedBukkit(i -> {
            ServerUtil.broadcastTitle(Title.builder().title("Title").build());
        });
    }
}
