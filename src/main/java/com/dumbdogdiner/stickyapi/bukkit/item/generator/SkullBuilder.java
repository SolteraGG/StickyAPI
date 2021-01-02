/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */

package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Base64;
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

    private String texture;


    public SkullBuilder() {

    }

    public SkullBuilder filter(@NotNull String group) {
        Preconditions.checkArgument(TextureHelper.getCategories().contains(group.toUpperCase()), "The specified group %s is not a valid filter", group);
        filter = group.toUpperCase();
        return this;
    }

    public SkullBuilder head(@NotNull String head) {
        head = head.toUpperCase();
        Preconditions.checkNotNull(TextureHelper.getTexture(filter, head), "The specified head %s is not a valid head", head);
        this.head = head;
        this.texture = TextureHelper.getTexture(filter, head);
        return this;
    }

    public SkullBuilder quantity(int i) {
        Preconditions.checkArgument(i >= 0 && i <= 64, "Invalid stack size of %s specified  (must be between 0 and 64, inclusive)", i);
        this.quantity = i;
        return this;
    }

    public SkullBuilder texture(@NotNull URL textureURL){
        // Java representation of the following JSON object:
        // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab"}}}
        class textures {
            SKIN skn;
            public textures(String url){
                skn = new SKIN(url);
            }
            class SKIN{
                String url;
                public SKIN(String s){
                    url = s;
                }
            }
        }

        texture(new String(Base64.getEncoder().encode(new Gson().toJson(new textures(textureURL.toString())).getBytes())));
        return this;
    }

    /**
     * Set the texture with a pre-encoded string
     * @param texture Base64 string of the json of texture location
     */
    SkullBuilder texture(String texture){
        Preconditions.checkArgument(TextureHelper.validateTexture(texture));
        this.texture = texture;
        return this;
    }

    public ItemStack build() {
        Preconditions.checkNotNull(texture);
        Preconditions.checkArgument(name != null || head != null);

        SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(new UUID(0, 0), null);

        profile.setName(TextureHelper.toQualifiedName(filter, head == null ? name : head));
        if(name != null){
            meta.setDisplayName(name);
        } else {
            meta.setDisplayName(StringUtil.capitalize(head));
        }

        profile.setProperty(new ProfileProperty("texture", texture));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, quantity);
        head.setItemMeta(meta);
        return head;
    }
}
