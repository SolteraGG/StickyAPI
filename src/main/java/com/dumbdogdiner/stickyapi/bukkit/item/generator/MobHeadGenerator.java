/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.common.util.textures.Mobhead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MobHeadGenerator {
    private SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
    private PlayerProfile profile = Bukkit.createProfile(new UUID(0,0), null);



    public MobHeadGenerator(Mobhead head){
        profile.setName(head.toDisplayString());
        profile.setProperty(new ProfileProperty("texture", head.getTexture()));
        meta.setPlayerProfile(profile);
    }

    public ItemStack getHead(){
        return getHead(1);
    }

    public ItemStack getHead(int amount){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, amount);
        head.setItemMeta(meta);
        return null;
    }
}
