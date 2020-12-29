/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.common.util.textures.MobHead;
import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SkullBuilder {
    private SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
    private PlayerProfile profile = Bukkit.createProfile(new UUID(0,0), null);
    private String filter = "*";
    private int quantity = 1;
    private String head;


    public SkullBuilder(MobHead head){
        profile.setName(head.getName());
        profile.setProperty(new ProfileProperty("texture", head.getTexture()));
        meta.setPlayerProfile(profile);
        meta.setDisplayName(head.getName());
    }

    public SkullBuilder filter(String group){
        if(TextureHelper.getCategories().contains(group.toUpperCase())){
            filter = group.toUpperCase();
        }
        return this;
    }

    public SkullBuilder head(String head){
        if(TextureHelper.getTexture(filter + "." + head) != null)
            this.head = head.toUpperCase();
        return this;
    }

    public SkullBuilder quantity(int i){
        if(i >= 0 && i <= 64){
            this.quantity = i;
        }
        return this;
    }

    public ItemStack build(){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, quantity);
        head.setItemMeta(meta);
        return null;
    }
}
