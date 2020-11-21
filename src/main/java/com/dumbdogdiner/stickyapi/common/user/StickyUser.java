/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.webapis.MojangAPI;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class StickyUser {
    @Getter
    private UUID uid;

    @Getter
    private String userName;


    public StickyUser(Player p){
        uid = p.getUniqueId();
        userName = p.getName();
    }

    public StickyUser(ProxiedPlayer p){
        uid = p.getUniqueId();
        userName = p.getName();
    }

    public StickyUser(OfflinePlayer p){
        uid = p.getUniqueId();
        userName = p.getName();
    }

    public Map<String, Instant> getNameHistory(){
        MojangAPI api = new MojangAPI(uid);
        return api.getUsernameHistory();
    }

    public Player getAsBukkitPlayer(){
        return Bukkit.getPlayer(uid);
    }

    public OfflinePlayer getAsOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uid);
    }
}
