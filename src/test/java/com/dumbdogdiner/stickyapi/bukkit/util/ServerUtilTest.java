/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */

package com.dumbdogdiner.stickyapi.bukkit.util;

import com.destroystokyo.paper.Title;
import com.dumbdogdiner.stickyapi_tests_common.BukkitCommon;
import org.junit.jupiter.api.Test;

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
