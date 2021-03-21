/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.textures.headdatabase;

import com.dumbdogdiner.stickyapi.common.ServerVersion;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.textures.InvalidTextureException;
import com.dumbdogdiner.stickyapi.util.textures.TextureHelper;
import com.dumbdogdiner.stickyapi.webapi.minecraftheads.MinecraftHeadsWebAPI;
import lombok.experimental.UtilityClass;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple basic interface to the minecraft-heads API plugin
 */
@UtilityClass
public class MinecraftHeadsAPI {
    private static final boolean usePlugin;
    private static final HeadDatabaseAPI mcheads;

    static {
        if(ServerVersion.isBukkit())
            if (Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) {
                Bukkit.getLogger().severe("HeadDatabase Plugin was not found!");
                usePlugin = false;
                mcheads = null;
            } else {
                mcheads = new HeadDatabaseAPI();
                usePlugin = true;
            }
        else {
            usePlugin = false;
            mcheads = null;
        }
    }

    /**
     * Takes in a HeadDatabase ID (see their head database command, ex: <pre>/hdb search id:43058</pre> and gets the associated texture
     * @param headId The HDB ID
     * @return The texture string of that head
     */
    public static String getTextureString(int headId) throws InvalidTextureException {
        if(usePlugin)
            return getTextureStringPlugin(headId);
        else {
            try {
                return MinecraftHeadsWebAPI.getTextureString(headId);
            } catch (HttpException e) {
                throw new InvalidTextureException(e);
            }
        }
    }

    /**
     * Uses the plugin to get the texture string
     * @param headId The HDB ID
     * @return The texture string for the head
     */
    private String getTextureStringPlugin(int headId) {
        return mcheads.getBase64(Integer.toString(headId));
    }

    /**
     * Takes in a HeadDatabase ID (see their head database command, ex: <pre>/hdb search id:43058</pre> and gets the associated name
     * @param headId The HDB ID
     * @return The name for the head
     */
    public static String getName(int headId) {
        if(usePlugin)
            return getNamePlugin(headId);
        else {
            try {
                return MinecraftHeadsWebAPI.getName(headId);
            } catch (HttpException e) {
                throw new InvalidTextureException(e);
            }
        }
    }

    private static String getNamePlugin(int headId) {
        ItemStack head = mcheads.getItemHead(Integer.toString(headId));
        ItemMeta headMeta = head.getItemMeta();
        if(headMeta.hasLocalizedName())
            return headMeta.getLocalizedName();
        else if(headMeta.hasDisplayName())
            return headMeta.getDisplayName();
        else
            return head.getI18NDisplayName();
    }

    /**
     * Takes in a HeadDatabase ID (see their head database command, ex: <pre>/hdb search id:43058</pre> and gets the associated texture URL
     * @param headId The HDB ID
     * @return The URL of the texture
     */
    public static URL getTextureUrl(int headId){
        try {
            return TextureHelper.decodeTextureStringToUrl(getTextureString(headId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Literally should never happen!
            throw new RuntimeException(e);
        }
    }
}
