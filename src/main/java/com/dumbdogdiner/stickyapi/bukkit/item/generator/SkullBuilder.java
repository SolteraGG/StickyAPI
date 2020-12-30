/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;


public class SkullBuilder {
    @Getter
    private String filter = "*";
    @Getter
    private int quantity = 1;
    @Getter
    private String head;
    @Accessors(fluent = true, chain = true)
    @Setter @Getter
    private String name;


    public SkullBuilder() {

    }

    public SkullBuilder filter(String group) {
        if (TextureHelper.getCategories().contains(group.toUpperCase())) {
            filter = group.toUpperCase();
        }
        return this;
    }

    public SkullBuilder head(String head) {
        head = head.toUpperCase();
        if (TextureHelper.getTexture(filter, head) != null) {
            this.head = head;
        }
        return this;
    }

    public SkullBuilder quantity(int i) {
        if (i >= 0 && i <= 64) {
            this.quantity = i;
        }
        return this;
    }

    public ItemStack build() {
        SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(new UUID(0, 0), null);

        profile.setName(TextureHelper.asQualifiedName(filter, head));
        if(name != null){
            meta.setDisplayName(name);
        } else {
            meta.setDisplayName(StringUtils.capitalize(head));
        }

        profile.setProperty(new ProfileProperty("texture", TextureHelper.getTexture(filter, head)));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, quantity);
        head.setItemMeta(meta);
        return head;
    }
}
