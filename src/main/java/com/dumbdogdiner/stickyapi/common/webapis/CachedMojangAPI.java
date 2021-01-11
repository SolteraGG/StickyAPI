/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

//TODO: Better error handeling in case of 404

//FIXME MAJOR BUG IN ASHCON: Sometimes the raw.value and the skin.data are inverted!
// Double check the occurances of this and whatnot, try to fix, etc.
public class CachedMojangAPI {
    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    protected static final @Nullable HttpUrl COMBINED_API_URL = HttpUrl.parse("https://api.ashcon.app/mojang/v2/user/");

    protected UUID uuid;

    public CachedMojangAPI(@NotNull String uuid) {
        this.uuid = StringUtil.hyphenateUUID(uuid);
    }

    public CachedMojangAPI(@NotNull OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(@NotNull Player player) {
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(@NotNull ProxiedPlayer player){
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(UUID uuid){
        this.uuid = uuid;
    }

    public @Nullable String getSkinTexture(){
        return getSkinTexture(this.uuid);
    }

    private static @NotNull Request buildRequest(@NotNull UUID uuid){
        return new Request.Builder().url(
                COMBINED_API_URL.newBuilder(
                        StringUtil.unhyphenateUUID(uuid))
                        .build())
                .build();
    }


    public static @Nullable String getSkinTexture(@NotNull StickyUser u){
        return getSkinTexture(u.getUniqueId());
    }
    public static @Nullable String getSkinTexture(@NotNull UUID uuid){
        try {
            @NotNull Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            if(resp.code() != 200)
                throw new java.net.ConnectException("A 404 was returned from " + buildRequest(uuid).url().url().toString() + "\n" + resp.toString());

            return (new Gson().fromJson(resp.body().charStream(), AshconResponse.class)).textures.raw.value;
            //return JsonParser.parseReader(resp.body().charStream()).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            StickyAPI.getLogger().severe(e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            return TextureHelper.getTexture("MHF.MHF_Steve");
        }
    }

    AshconResponse getResponse(){
        try {
            @NotNull Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            if(resp.code() != 200)
                throw new IOException("A 404 was returned from " + buildRequest(uuid).url().url().toString() + "\n" + resp.toString());
            return (new Gson().fromJson(resp.body().charStream(), AshconResponse.class));
        } catch (IOException ioe){
            StickyAPI.getLogger().severe(Arrays.toString(ioe.getStackTrace()));
            return new AshconResponse();
        }
    }

    @Deprecated
    public JsonElement getFullJsonCombinedAPI(){
        try {
            return getJSONFromURL(new URL(COMBINED_API_URL + uuid.toString().replace("-","")));
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return new JsonObject();
        }
    }

    public @NotNull HashMap<String, Instant> getUsernameHistory(){
        @NotNull HashMap<String, Instant> retval = new HashMap<>();
        try{
            @NotNull URL url = new URL(COMBINED_API_URL + uuid.toString().replace("-",""));
            for (@NotNull JsonElement el : getJSONFromURL(url).getAsJsonObject().getAsJsonArray("username_history")) {
                @Nullable Instant changedAt = null;
                if(el.getAsJsonObject().has("changed_at")){

                    String datestr = el.getAsJsonObject().get("changed_at").getAsString();
                    changedAt = Instant.parse(datestr);

                }
                retval.put(el.getAsJsonObject().get("username").getAsString(), changedAt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retval;
    }

    private JsonElement getJSONFromURL(@NotNull URL url){

        try {
            return new JsonParser().parse(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }

    public @Nullable String getUsername() {
        try {
            //Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            return getResponse().username;
        } catch (Exception e) {
            StickyAPI.getLogger().severe(e.getMessage());
            return null; //STEVE_TEXTURE;
        }
    }
/*

            String uuid = jsonResponse.getAsJsonObject().get("uuid").toString().replace("\"", "");
            String username = jsonResponse.getAsJsonObject().get("username").toString().replace("\"", "");
            String skinUrl = jsonResponse.getAsJsonObject().get("textures").getAsJsonObject().get("raw").getAsJsonObject().get("value").getAsString();

            if (uuid == null)
                return null;
            return new MojangUser(username, UUID.fromString(uuid), skinUrl);
 */
}
