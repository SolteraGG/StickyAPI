/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.user;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import com.dumbdogdiner.stickyapi.common.webapis.MojangAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class StickyUserBungee extends StickyUser {
    public StickyUserBungee(UUID uniqueId) {
        super(uniqueId);
    }

    public StickyUserBungee(ProxiedPlayer p) {
        super(p.getUniqueId(), p.getName());
    }
}
