/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.mojang;

import com.dumbdogdiner.stickyapi.annotation.Broken;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.util.http.HttpConnectionException;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpStatusException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;


// Double check the occurances of this and whatnot, try to fix, etc.
public class MojangAPI {
    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    private static final @NotNull Gson G = HttpUtil.getDefaultGsonInstance();
    private static final @NotNull OkHttpClient CLIENT = HttpUtil.getDefaultClientInstance();


    protected static final @NotNull HttpUrl COMBINED_API_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("api.ashcon.app")
            .addPathSegments("mojang/v2/user/")
            .build();


    private static final HttpUrl MOJANG_STATUS_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("status.mojang.com")
            .addPathSegment("check")
            .build();
    @NotNull
    private static final HttpUrl MOJANG_API_BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("api.mojang.com")
            .build();

    /**
     * The (old) url for the Mojang session server
     */
    @Deprecated
    private static final HttpUrl MOJANG_SESSION_URL_OLD = new HttpUrl.Builder()
            .scheme("https")
            .host("sessionserver.mojang.com")
            .build();

    private static final HttpUrl MOJANG_SESSION_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("session.minecraft.net")
            .build();

    private static @NotNull Response getResponse(String param) throws HttpException {
        Request ashconRequest = new Request.Builder().url(
                COMBINED_API_URL.newBuilder()
                        .addPathSegment(param)
                        .build())
                .build();
        try {
            Response resp = CLIENT.newCall(ashconRequest).execute();
            if (resp.code() == 200)
                return resp;
            throw new HttpStatusException(resp);
        } catch (IOException e) {
            throw new HttpConnectionException(ashconRequest, e);
        }
    }

    @Deprecated
    public static @NotNull JsonObject getJsonResponse(UUID uniqueId) throws HttpException {
        return JsonParser.parseReader(getResponse(StringUtil.unhyphenateUUID(uniqueId)).body().charStream()).getAsJsonObject();
    }

    private static @NotNull AshconResponse getAshconResponse(UUID uniqueId) throws HttpException {
        return getAshconResponse(StringUtil.unhyphenateUUID(uniqueId));
    }

    private static @NotNull AshconResponse getAshconResponse(String param) throws HttpException {
        return G.fromJson(getResponse(param).body().charStream(), AshconResponse.class);
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

    public static @NotNull UUID getUniqueId(String username) throws HttpException {
        return getAshconResponse(username).getUniqueId();
    }

    public static @NotNull UUID getUniqueId(@NotNull String username, @NotNull Instant when) throws HttpException{
        return uuidAtTime(username, when);
    }


    public static @NotNull Map<String, MojangStatus> getMojangAPIStatus() throws HttpException {
        @NotNull Map<String, MojangStatus> status = new HashMap<>();

        for (@NotNull JsonElement obj : HttpUtil.getResponseAsJson(MOJANG_STATUS_URL).getAsJsonArray()) {
            for (Map.@NotNull Entry<String, JsonElement> entry : obj.getAsJsonObject().entrySet()) {
                status.put(entry.getKey(), MojangStatus.valueOf(entry.getValue().getAsString().toUpperCase()));
            }
        }

        return status;
    }

    /**
     * Get the created time of an account. Presently sorta broken, if the time is not available it will return an instant of -1
     * @param uniqueId The UUID to get the created time for
     * @return The created time as an instant if possible, or an Instant representing -1 Seconds Unix time if not
     */
    public static @NotNull Instant getCreated(UUID uniqueId) throws HttpException{
        return getAshconResponse(uniqueId).getCreated();
    }

    /**
     * Provides the UUID associated with a given username at a given time.
     * FIXME: This is currently broken, with an <a href="https://bugs.mojang.com/browse/WEB-3367>open bug report at Mojang</a>
     * @param username The username to check
     * @param timestamp The timestamp of when you wish to check. FIXME: Currently, Mojang sets this to 0 due to a bug
     * @return The UUID attatched to the username at the specified time
     */
    @Broken(issuepage = "https://bugs.mojang.com/browse/WEB-3367")
    public static @NotNull UUID uuidAtTime(@NotNull String username, @NotNull Instant timestamp) throws HttpException {
        @NotNull HttpUrl url = MOJANG_API_BASE_URL.newBuilder()
                .addPathSegments("users/profile/minecraft/")
                .addPathSegment(username)
                .addQueryParameter("at", Long.toString(timestamp.getEpochSecond()))
                .build();

        return StringUtil.hyphenateUUID(HttpUtil.getResponseAsJson(url).getAsJsonObject().get("id").getAsString());
    }
}
