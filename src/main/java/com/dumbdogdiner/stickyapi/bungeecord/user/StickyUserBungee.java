/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.user;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.time.Instant;
import java.util.UUID;

public class StickyUserBungee extends StickyUser {
    public StickyUserBungee(UUID uniqueId) {
        super(uniqueId);
    }

    public StickyUserBungee(ProxiedPlayer p) {
        super(p);
    }

    protected StickyUserBungee(UUID uniqueId, String userName) {
        super(uniqueId, userName);
    }

    public Server getConnectedServer(){
        return getAsProxiedPlayer().getServer();
    }
}
