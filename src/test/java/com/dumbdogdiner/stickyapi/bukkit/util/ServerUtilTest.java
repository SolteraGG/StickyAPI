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
import com.dumbdogdiner.stickyapi_tests_common.BukkitCommon;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ServerUtilTest {



    @Test
    public void testBroadcastMessage() {
        BukkitCommon.getMockedBukkit(i -> {
            ServerUtil.broadcastMessage("hi");
        });
    }

    @Test
    public void testBroadcastTitle() {
        BukkitCommon.getMockedBukkit(i -> {
            ServerUtil.broadcastTitle(Title.builder().title("Title").build());
        });
    }
}
