/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.user;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StickyUserBungee extends StickyUser {
    public StickyUserBungee(@NotNull UUID uniqueId) {
        super(uniqueId);
    }

    public StickyUserBungee(@NotNull ProxiedPlayer p) {
        super(p.getUniqueId(), p.getName());
    }

    public ProxiedPlayer toProxiedPlayer(){
        return ProxyServer.getInstance().getPlayer(uniqueId);
    }
}
