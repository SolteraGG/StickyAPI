/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis.mojang;

import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.common.webapis.HttpConnectionException;
import com.dumbdogdiner.stickyapi.common.webapis.HttpException;
import com.dumbdogdiner.stickyapi.common.webapis.HttpStatusException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

//TODO: Better error handeling in case of 404

// FIXME POSSIBLE MAJOR BUG IN ASHCON: Sometimes the raw.value and the skin.data are inverted!
// Double check the occurances of this and whatnot, try to fix, etc.
public class CachedMojangAPI {
    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    private static final @NotNull Gson G = new Gson();
    private static final @NotNull OkHttpClient HTTP_CLIENT = new OkHttpClient();

    protected static final @NotNull HttpUrl COMBINED_API_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("api.ashcon.app")
            .addPathSegments("mojang/v2/user/")
            .build();


    private static @NotNull Request buildAshconRequest(@NotNull UUID uniqueId) {
        return new Request.Builder().url(
                COMBINED_API_URL.newBuilder()
                        .addPathSegment(StringUtil.unhyphenateUUID(uniqueId))
                        .build())
                .build();
    }

    private static @NotNull Response getResponse(UUID uniqueId) throws HttpException {
        Request ashconRequest = buildAshconRequest(uniqueId);
        try {
            Response resp = HTTP_CLIENT.newCall(ashconRequest).execute();
            if (resp.code() == 200)
                return resp;
            throw new HttpStatusException(resp);
        } catch (IOException e) {
            throw new HttpConnectionException(ashconRequest, e);
        }
    }

    @Deprecated
    public static @NotNull JsonObject getJsonResponse(UUID uniqueId) throws HttpException {
        return JsonParser.parseReader(getResponse(uniqueId).body().charStream()).getAsJsonObject();
    }


    private static @NotNull AshconResponse getAshconResponse(UUID uniqueId) throws HttpException {
        return G.fromJson(getResponse(uniqueId).body().charStream(), AshconResponse.class);
    }

    /**
     * Gets the history of usernames that a given {@link UUID} has had, by time
     * @param uniqueId The {@link UUID} of the user to check
     * @return A {@link SortedMap} of the time of each change (With 1970-01-01T00:00:00Z meaning that it was the first Username)
     * @throws HttpException if there is an error with the HTTP Request/Response
     */
    public static @NotNull SortedMap<Instant, String> getUsernameHistory(UUID uniqueId) throws HttpException {
        return getAshconResponse(uniqueId).getUsernameHistory();

    }

    
    public static byte[] getTexture(@NotNull UUID uniqueId) throws HttpException {
        return getAshconResponse(uniqueId).getTextures().getSkin().getData();
    }

    public static @Nullable String getTextureString(@NotNull UUID uniqueId) throws HttpException {
        return getAshconResponse(uniqueId).getTextureString();
    }


    public static @NotNull String getUsername(UUID uniqueId) throws HttpException {
        return getAshconResponse(uniqueId).getUsername();
    }

    public static @Nullable UUID getUniqueId(String username) {
        throw new UnsupportedOperationException();
    }

    public static @Nullable UUID getUniqueId(String username, Instant when) {
        throw new UnsupportedOperationException();
    }


    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */


    private static final HttpUrl MOJANG_STATUS_BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("status.mojang.com")
            .addPathSegment("check")
            .build();
    @NotNull
    protected static final HttpUrl MOJANG_API_BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("api.mojang.com")
            .build();

    protected static final String MOJANG_SESSION_URL = "https://sessionserver.mojang.com";


    public static @NotNull Map<String, MojangStatus> getMojangAPIStatus() throws HttpConnectionException {
        @NotNull Map<String, MojangStatus> status = new HashMap<>();
        Request request = new Request.Builder().url(MOJANG_STATUS_BASE_URL).build();
        try {
            Response resp = HTTP_CLIENT.newCall(request).execute();
            for (@NotNull JsonElement obj : JsonParser.parseString(resp.body().string()).getAsJsonArray()) {
                for (Map.@NotNull Entry<String, JsonElement> entry : obj.getAsJsonObject().entrySet()) {
                    status.put(entry.getKey(), MojangStatus.valueOf(entry.getValue().getAsString().toUpperCase()));
                }
            }
        } catch (IOException e) {
            throw new HttpConnectionException(request, e);
        }
        return status;
    }

    public static @NotNull UUID uuidAtTime(@NotNull String username, @NotNull Instant timestamp) throws HttpException {
        @NotNull HttpUrl url = MOJANG_API_BASE_URL.newBuilder()
                .addPathSegments("users/profile/minecraft/")
                .addPathSegment(username)
                .addQueryParameter("at", Long.toString(timestamp.getEpochSecond()))
                .build();
        try {
            @NotNull Response resp = HTTP_CLIENT.newCall(new Request.Builder().url(url).build()).execute();
            if (resp.code() == HttpURLConnection.HTTP_OK) {
                JsonObject responseJson = JsonParser.parseReader(resp.body().charStream()).getAsJsonObject();
                return StringUtil.hyphenateUUID(responseJson.get("id").getAsString());
            } else {
                throw new HttpStatusException(resp);
            }
        } catch (IOException e) {
            throw new HttpConnectionException(url, e);
        }
    }
}
