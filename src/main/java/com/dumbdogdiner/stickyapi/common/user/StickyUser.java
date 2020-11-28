/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.bukkit.user.StickyUserBukkit;
import com.dumbdogdiner.stickyapi.common.cache.Cacheable;
import com.dumbdogdiner.stickyapi.webapis.MojangAPI;
import lombok.Getter;
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

    @Getter
    protected String name;

    public StickyUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        MojangAPI a = new MojangAPI(uniqueId);
        name = a.getUsername();
    }

    public StickyUser(Player p){
        uniqueId = p.getUniqueId();
        name = p.getName();
    }

    public StickyUser(ProxiedPlayer p){
        uniqueId = p.getUniqueId();
        name = p.getName();
    }

    public StickyUser(OfflinePlayer p){
        uniqueId = p.getUniqueId();
        name = p.getName();
    }

    protected StickyUser(UUID uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.name = userName;
    }

    public Map<String, Instant> getNameHistory(){
        MojangAPI api = new MojangAPI(uniqueId);
        return api.getUsernameHistory();
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
