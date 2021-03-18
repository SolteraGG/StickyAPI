/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

import com.dumbdogdiner.stickyapi.util.http.HttpConnectionException;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.dumbdogdiner.stickyapi.util.reflection.ReflectionUtil;
import com.dumbdogdiner.stickyapi.util.textures.TextureHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Cat;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple basic interface to the minecraft-heads API plugin
 */
@UtilityClass
public class MinecraftHeadsAPI {
    private static final HeadDatabaseAPI mcheads;
    static {
        if(Bukkit.getPluginManager().getPlugin("HeadDatabase") != null){
            Bukkit.getLogger().severe("HeadDatabase Plugin was not found!");
            mcheads = null;
        } else {
            mcheads = new HeadDatabaseAPI();
        }
    }

    /**
     * Takes in a HeadDatabase ID (see their head database command, ex: <pre>/hdb search id:43058</pre> and gets the associated texture
     */
    public String getTextureString(int headId) {
        return mcheads.getBase64(Integer.toString(headId));
    }

    public String getName(int headId){
        ItemStack head = mcheads.getItemHead(Integer.toString(headId));
        ItemMeta headMeta = head.getItemMeta();
        if(headMeta.hasLocalizedName())
            return headMeta.getLocalizedName();
        else if(headMeta.hasDisplayName())
            return headMeta.getDisplayName();
        else
            return head.getI18NDisplayName();
    }

    public URL getTextureUrl(int headId){
        try {
            return TextureHelper.decodeTextureStringToUrl(getTextureString(headId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Should not happen tbh
            return null;
        }
    }
}
