/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.bukkit.user.StickyUserBukkit;
import com.dumbdogdiner.stickyapi.common.cache.Cacheable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StickyUser implements Cacheable {
    @Getter
    protected UUID uniqueId;

    @Getter
    protected String name;

    public StickyUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.name = null;
    }

    public StickyUser(StickyUser p){
        uniqueId = p.getUniqueId();
        name = p.getName();
    }

    protected StickyUser(UUID uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.name = userName;
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
