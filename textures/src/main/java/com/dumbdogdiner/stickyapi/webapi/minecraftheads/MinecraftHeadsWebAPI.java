/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;


import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basic scraping API wrapper for the minecraft-heads website
 * For now, it doesn't use their API, because their API does not provide IDs
 */
public class MinecraftHeadsWebAPI {
    private static final @NotNull HttpUrl CUSTOM_HEAD_BASE = Objects.requireNonNull(HttpUrl.parse("https://minecraft-heads.com/custom-heads/"));
    private static final @NotNull HttpUrl API_URL = Objects.requireNonNull(HttpUrl.parse("https://minecraft-heads.com/scripts/api.php"));
    /**
     * Gets the texture string for a given HeadID, using the web api
     * @param headId The HDB ID
     * @return The texture string
     * @throws HttpException if something goes wrong accessing the web api
     */
    public static String getTextureString(int headId) throws HttpException {
        return getDocument(headId).body().getElementById("UUID-Value").text();
    }

    /**
     * Gets the name for a given HeadID, using the web api
     * @param headId The HDB ID
     * @return The name
     * @throws HttpException if something goes wrong accessing the web api
     */
    public static String getName(int headId) throws HttpException {
        return getDocument(headId).title();
    }

    private static Document getDocument(int headId) throws HttpException {
        try {
            Response response = HttpUtil.getResponse(CUSTOM_HEAD_BASE.newBuilder().addPathSegment(Integer.toString(headId)).build());
            return Jsoup.parse(response.body().string());
        } catch (IOException e){
            throw new HttpException("Error decoding response body", e);
        }
    }

    /**
     * Gets a list of all heads of a given category; Consider caching the result as it is expensive!
     * @param category Which category to query; If this parameter is null, then all categories are queried
     * @param withTags If tags should be included with the heads
     * @return A list containing all the info from the API about the heads of the category
     */
    public static List<HeadInfo> getHeads(@Nullable Category category, boolean withTags) throws HttpException{
        List<HeadInfo> heads = new ArrayList<>();
        if(category == null){
            for(Category cat : Category.values()){
                heads.addAll(getHeads(cat, withTags));
            }
        } else {
            HttpUrl url = API_URL.newBuilder()
                    .addQueryParameter("cat", category.toWebString())
                    .addQueryParameter("tags", String.valueOf(withTags))
                    .build();
            JsonElement resp = HttpUtil.getResponseAsJson(url);
            if(!resp.isJsonArray())
                throw new HttpException(url, "Invalid content received");

            JsonArray headsArray = resp.getAsJsonArray();

            for(JsonElement el : headsArray){
                heads.add(HttpUtil.getDefaultGsonInstance().fromJson(el, HeadInfo.class));
            }
        }
        return heads;
    }
}
