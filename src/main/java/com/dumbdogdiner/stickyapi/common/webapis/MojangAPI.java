/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.util.textures.DefaultSkins;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

//TODO: Better error handeling in case of 404

public class MojangAPI {


    /** When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    protected static final String MOJANG_STATUS_BASE_URL = "https://status.mojang.com/check";
    protected static final String MOJANG_API_BASE_URL = "https://api.mojang.com";

    protected static final String MOJANG_SESSION_URL = "https://sessionserver.mojang.com";

    protected UUID uuid;

    public MojangAPI(String uuid) {
        this.uuid = StringUtil.hyphenateUUID(uuid);
    }

    public MojangAPI(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }

    public MojangAPI(Player player) {
        this.uuid = player.getUniqueId();
    }

    public MojangAPI(UUID uuid){
        this.uuid = uuid;
    }

    public static Map<String, MojangStatus>  getMojangAPIStatus(){
        Map<String, MojangStatus> status = new HashMap<>();
        try {
            Response resp = HTTP_CLIENT.newCall(new Request.Builder().url(MOJANG_STATUS_BASE_URL).build()).execute();
            for(JsonElement obj : JsonParser.parseReader(resp.body().charStream()).getAsJsonArray()){
                for(Map.Entry<String, JsonElement> entry : obj.getAsJsonObject().entrySet()){
                    status.put(entry.getKey(), MojangStatus.valueOf(entry.getValue().getAsString().toUpperCase()));
                }
            }
        } catch (Exception e){
            StickyAPI.getLogger().log(Level.WARNING, e.getMessage());
        }
        return status;
    }

    public String getSkinTexture(){
        try {
            URL url = new URL(MOJANG_API_BASE_URL + "/" + uuid.toString().replace("-",""));
            return  getJSONFromURL(url).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return DefaultSkins.STEVE.getTexture();
        }
    }

    public JsonElement getFullJsonCombinedAPI(){
        try {
            return getJSONFromURL(new URL(MOJANG_API_BASE_URL + "/" + uuid.toString().replace("-","")));
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return new JsonObject();
        }
    }

    public HashMap<String, Instant> getUsernameHistory(){
        HashMap<String, Instant> retval = new HashMap<>();
        try{
            URL url = new URL(MOJANG_API_BASE_URL + "/" + uuid.toString().replace("-",""));
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

    public String getUsername() {
        try {
            URL url = new URL(MOJANG_API_BASE_URL + "/" + uuid.toString().replace("-",""));
            return  getJSONFromURL(url).getAsJsonObject().get("username").getAsString();//.getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return null;//STEVE_TEXTURE;
        }
    }

    private JsonElement getJSONFromURL(URL url){

        try {
            return new JsonParser().parse(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
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
