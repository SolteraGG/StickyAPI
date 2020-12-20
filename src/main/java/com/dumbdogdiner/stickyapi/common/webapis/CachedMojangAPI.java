/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.util.textures.DefaultSkins;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

//TODO: Better error handeling in case of 404

public class CachedMojangAPI {
    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    protected static final String MOJANG_STATUS_BASE_URL = "https://status.mojang.com/check";
    protected static final String MOJANG_API_BASE_URL = "https://api.mojang.com";
    protected static final HttpUrl COMBINED_API_URL = HttpUrl.parse("https://api.ashcon.app/mojang/v2/user/");
    protected static final String MOJANG_SESSION_URL = "https://sessionserver.mojang.com";

    protected UUID uuid;

    public CachedMojangAPI(String uuid) {
        this.uuid = StringUtil.hyphenateUUID(uuid);
    }

    public CachedMojangAPI(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(Player player) {
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(ProxiedPlayer player){
        this.uuid = player.getUniqueId();
    }

    public CachedMojangAPI(UUID uuid){
        this.uuid = uuid;
    }

    public String getSkinTexture(){
        return getSkinTexture(this.uuid);
    }

    private static Request buildRequest(UUID uuid){
        return new Request.Builder().url(COMBINED_API_URL.newBuilder(StringUtil.unhyphenateUUID(uuid)).build()).build();
    }


    public static String getSkinTexture(StickyUser u){
        return getSkinTexture(u.getUniqueId());
    }
    public static String getSkinTexture(UUID uuid){
        try {
            Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            if(resp.code() != 200)
                throw new IOException("A 404 was returned from " + buildRequest(uuid).url().url().toString() + "\n" + resp.toString());

            return (new Gson().fromJson(resp.body().charStream(), AshconResponse.class)).textures.raw.value;
            //return JsonParser.parseReader(resp.body().charStream()).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            return DefaultSkins.STEVE.getTexture();
        }
    }

    AshconResponse getResponse(){
        try {
            Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            if(resp.code() != 200)
                throw new IOException("A 404 was returned from " + buildRequest(uuid).url().url().toString() + "\n" + resp.toString());
            return (new Gson().fromJson(resp.body().charStream(), AshconResponse.class));
        } catch (IOException ioe){
            Bukkit.getLogger().severe(Arrays.toString(ioe.getStackTrace()));
            return new AshconResponse();
        }
    }

    @Deprecated
    public JsonElement getFullJsonCombinedAPI(){
        try {
            return getJSONFromURL(new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-","")));
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return new JsonObject();
        }
    }

    public HashMap<String, Instant> getUsernameHistory(){
        HashMap<String, Instant> retval = new HashMap<>();
        try{
            URL url = new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-",""));
            for (JsonElement el : getJSONFromURL(url).getAsJsonObject().getAsJsonArray("username_history")) {
                Instant changedAt = null;
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

    private JsonElement getJSONFromURL(URL url){

        try {
            return new JsonParser().parse(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }

    public String getUsername() {
        try {
            Response resp = HTTP_CLIENT.newCall(buildRequest(uuid)).execute();
            return  JsonParser.parseReader(resp.body().charStream()).getAsJsonObject().get("username").getAsString();//.getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return null;//STEVE_TEXTURE;
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
