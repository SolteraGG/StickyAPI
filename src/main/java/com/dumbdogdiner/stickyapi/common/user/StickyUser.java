/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.bukkit.user.StickyUserBukkit;
import com.dumbdogdiner.stickyapi.common.ServerVersion;
import com.dumbdogdiner.stickyapi.common.cache.Cacheable;
import com.dumbdogdiner.stickyapi.common.webapis.CachedMojangAPI;
import com.dumbdogdiner.stickyapi.common.webapis.MojangAPI;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class StickyUser implements Cacheable {
    @Getter
    protected UUID uniqueId;

    protected CachedMojangAPI mojangAPI;

    @Getter
    protected String name;

    public StickyUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.mojangAPI = new CachedMojangAPI(uniqueId);
        this.name = mojangAPI.getUsername();
    }

    public StickyUser(StickyUser p){
        uniqueId = p.getUniqueId();
        name = p.getName();
        mojangAPI = p.mojangAPI;
    }

    protected StickyUser(UUID uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.name = userName;
        mojangAPI = new CachedMojangAPI(uniqueId);
    }

    public Map<String, Instant> getNameHistory(){
        return mojangAPI.getUsernameHistory();
    }

    public Player getAsBukkitPlayer(){
        return Bukkit.getPlayer(uniqueId);
    }

    public OfflinePlayer getAsOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    public StickyUserBukkit getAsBukkitUser(){
        return new StickyUserBukkit(this);
    }

    @Override
    public String getKey() {
        return uniqueId.toString();
    }
}
