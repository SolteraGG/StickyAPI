/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.dumbdogdiner.stickyapi.bukkit.user.StickyUserBukkit;
import com.dumbdogdiner.stickyapi.common.webapis.CachedMojangAPI;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;
import java.util.UUID;

public class PlayerHeadBuilder extends SkullBuilder {
    private SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
    private StickyUserBukkit player;

    private PlayerProfile ownerProfile;

    public PlayerHeadBuilder(UUID playerId){
        this.meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerId));
        this.ownerProfile = Bukkit.getServer().createProfile(playerId);
        this.player = new StickyUserBukkit(playerId);
    }

    public PlayerHeadBuilder(Player player){
        this(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public PlayerHeadBuilder(StickyUserBukkit player){
        this.meta.setOwningPlayer(player.getAsBukkitPlayer());
        this.ownerProfile = player.getAsBukkitPlayer().getPlayerProfile();
        this.player = new StickyUserBukkit(player);
    }

    public PlayerHeadBuilder(OfflinePlayer player){
        this.meta.setOwningPlayer(player);
        this.ownerProfile = Bukkit.createProfile(player.getUniqueId());
        this.player = new StickyUserBukkit(player);
    }

    public PlayerHeadBuilder(ItemStack head){
        Preconditions.checkArgument(head.getType() != Material.PLAYER_HEAD
                && head.getType() != Material.PLAYER_WALL_HEAD,
                "Head must be a player head or player wall head");
        meta = (SkullMeta) head.getItemMeta();
        ownerProfile = meta.getPlayerProfile();
        name(meta.getDisplayName());
        if(!Objects.requireNonNull(ownerProfile).hasTextures()){
            if(!ownerProfile.complete()){
                throw new IllegalArgumentException("Invalid player profile attached to the head, with no UUID or textures!");
            }
        }
        player = new StickyUserBukkit(ownerProfile.getId());
    }

    public PlayerHeadBuilder freeze(){
        texture(CachedMojangAPI.getSkinTexture(player));
        return this;
    }
}
