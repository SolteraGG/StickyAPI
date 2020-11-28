/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.user;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StickyUserBukkit extends StickyUser {
    public StickyUserBukkit(Player p) {
        super(p);
    }

    public StickyUserBukkit(OfflinePlayer p) {
        super(p);
    }

    public StickyUserBukkit(StickyUser p){
        super(p.getUniqueId(), p.getName());
    }

    public StickyUserBukkit(UUID uniqueId){
        super(Bukkit.getOfflinePlayer(uniqueId));
    }

    public boolean isOnline(){
        return getAsOfflinePlayer().isOnline();
    }

    public boolean isBanned(){
        return getAsOfflinePlayer().isBanned();
    }

    public boolean playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().playSound(location, sound, v, v1);
        return true;
    }

    public boolean sendMessage(String [] msgs){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendMessage(msgs);
        return true;
    }

    public boolean sendMessage(String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendMessage(msg);
        return true;
    }

    public boolean sendRawMessage(String msg){
        if(!isOnline())
            return false;
        getAsBukkitPlayer().sendRawMessage(msg);
        return true;
    }




}
