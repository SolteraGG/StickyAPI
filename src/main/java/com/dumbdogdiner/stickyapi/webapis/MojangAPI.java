/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapis;

import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class MojangAPI {

    /**
     * When possible, use the cached, faster api at
     * https://api.ashcon.app/mojang/v2/user, otherwise use mojang API URLs
     */

    public enum APIStatus {
        GREEN, YELLOW, RED
    }

    protected static final String MOJANG_STATUS_BASE_URL = "https://status.mojang.com/check";
    protected static final String MOJANG_API_BASE_URL = "https://api.mojang.com";
    protected static final String COMBINED_API_URL = "https://api.ashcon.app/mojang/v2/user";
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

    public MojangAPI(UUID uuid) {
        this.uuid = uuid;
    }

    public static APIStatus getMojangAPIStatus() {
        return APIStatus.RED;
    }

    public String getSkinTexture() {
        try {
            URL url = new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-", ""));
            return getJSONFromURL(url).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("raw").get("value")
                    .getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return STEVE_TEXTURE;
        }
    }

    public JsonElement getFullJsonCombinedAPI() {
        try {
            return getJSONFromURL(new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-", "")));
        } catch (MalformedURLException e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return new JsonObject();
        }
    }

    public HashMap<String, Instant> getUsernameHistory() {
        HashMap<String, Instant> retval = new HashMap<>();
        try {
            URL url = new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-", ""));
            for (JsonElement el : getJSONFromURL(url).getAsJsonObject().getAsJsonArray("username_history")) {
                Instant changedAt = null;
                if (el.getAsJsonObject().has("changed_at")) {

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
            URL url = new URL(COMBINED_API_URL + "/" + uuid.toString().replace("-", ""));
            return getJSONFromURL(url).getAsJsonObject().get("username").getAsString();// .getAsJsonObject("textures").getAsJsonObject("raw").get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Arrays.toString(e.getStackTrace()));
            return null;// STEVE_TEXTURE;
        }
    }

    private JsonElement getJSONFromURL(URL url) {

        try {
            return new JsonParser().parse(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }
    /*
     * 
     * String uuid =
     * jsonResponse.getAsJsonObject().get("uuid").toString().replace("\"", "");
     * String username =
     * jsonResponse.getAsJsonObject().get("username").toString().replace("\"", "");
     * String skinUrl =
     * jsonResponse.getAsJsonObject().get("textures").getAsJsonObject().get("raw").
     * getAsJsonObject().get("value").getAsString();
     * 
     * if (uuid == null) return null; return new MojangUser(username,
     * UUID.fromString(uuid), skinUrl);
     */

    public static final String STEVE_TEXTURE = "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";
    public static final String ALEX_TEXTURE = "\"iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFhElEQVR4Xu1a328UVRjdR6UUKJFIKL90oa4Vs1TwgRItUvlljMYSjVIDBAzUloZkg5pIDFWJUE3UpyaQiokJSWM0PPjrwcAjT/2fPufc2TN++82dGcruTrfbOcnJ3L33u7f3nPvd2dm5LZUyMLitV8Bdm59wV5bBHRt65JNj1VTa8VYcKLi6Y72jNgCEyMlDe+Wbk/ujK9hVBlD8wJY1iQaUy+UGoq4rDODq223Auq43QItFBoB6O6wKA3ziswwYqwx0jwEU/vzWta6Mu781wMeuMYDCNZkZ+ivPRztex6HSv0ZAiHquv8eJfam8QV7cGbK6s1de2B7eBypbe9yNELGogwGMQx/0xRho57g0Kuk5ws4nd2gDwD3b1zkhELhvV5+7kvh8eHCjjAxucnEwhDHogzqOow1Ie46w88kdbtKbw1ULhQfC+rHS/Nwrf89ekvvf1+Th3Izc/64WlK/Ib5+ekv27N7oYxKIP+kbbJRgTY0Nw2nOEnU/ucCu1JUjlbevCVe0PtwLK1Wf75NcvLsif16fd9feZaZmvfSj/ztZk/uM3HRGDWPRBX5QxFsbE56znCDuf3MFUDbfBWik//aTceH9Y5s8fkeHKU048MPbKrPx19aITjzIAUxCDWPRBX4yhx9RiuT30drDzyR3RZIMVC9O2T66PDcncmdfk5qkDgbij8sfVc/LPV7Xo6+3BzUvyy+QxmQme+RGDWPRBX4zB1acBPvEdYwDTHml7brQqP02+LbfPvx79oPnx9Ggg8JB8dvjlBv5w+lX58uQBF4NY9EFfjOG2QH07UHzSc4SdT+7grzeKnr943K3otx8clCvHq3L5yB6ZHh10YsHP39onX7970BFlxCD254kTru/tj94Ir/XxaCQzgKRxdj7LjvdOLIpmpVJpoI2PYXEx9jAUPRQFbaWzZ9OZhYWFcBwwKGNLvjO0O7ra8CWjFQbYR2KyJQZQfJ2r24AgA7R49zeaRSsMsKnfzi3QtAF7KzUBB54Zd9ejw3cbqNts+/DQrJSuXfuf4+NSunVLSnfuuBsmvip580Sdo44HUQdR5L17Ifk5KZ4xtp39ySxQ4OMYALo/OjEREmVOvG5CJJ4T0vGgFq3JcXQsBbIPDbXtj2NAksCsdu8EOUlMEBmhRep40E5Ykyusx7eG2XY7RhayBGa1u7TnBFDWf5wG6LqkeMZBoC5bgTqb8soAHYN9rxkJITF5LZpl1utYGmBFq/54KCNvBA9f+P2hiTodY9ut3hh8BlCczoBEA7QYmKEN8AnThnEL+PrUU91nwNzl8Ugg61tqgM2AtPaYILv63LPWAKatR7Q2RIuzBuDqM8DV12Os3hgoLElgpgF2D2YZYL8FfLEJBtgVtgb4tojVG8OjGJDWHjOAgqwofk6L91CLSzLAZsiSDBgZGREwSSDrGTc1NdXASBCuzABSr36SATqeMSwH/bQ4a4AzQRnAdr1FrN4YKCxJYFa73iK4WgNBd7NUBuibKttdDLdJQN6EkwygSF+GLJsBvgxqMCEQlhbPGF1nV7jhBhjQd49YkgEFChQoUKBAgQIFChQoUKBAgQLNounDVbwVauXhZ95o2gBz/r/6DNAZsNiGf4BoN1pqwEIb/gWm1eDLTf2WV9O+BOXbXjJ6nU7qV+m+/yewZwfLjUc1IKndidLnBDz9pRG6XZ8krSQD0tpjBmhxMCGtvSsNaPX5f7uhxfkEWgPs8bo+1PCd7fkOPjTtfHIHxfmOupZiQNbRV0cbkJbiWe3WAH2sZQ1ghvBYbEUZQNp2e7hJYVEmqNX3bRE7n9yRJTCr3aa4zwBtUscZwFPjJIGsTzpdbhB/Jr7HG7JDGdQxWyDr+Dyr3bfCOgtWhQGkE6pucK5sMqTVBvwH+QeX13iz8VkAAAAASUVORK5CYII=\"";
}
