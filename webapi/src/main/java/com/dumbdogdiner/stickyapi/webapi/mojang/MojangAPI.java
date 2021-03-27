/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.mojang;

import com.dumbdogdiner.stickyapi.annotation.Broken;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.dumbdogdiner.stickyapi.StickyAPI.getLogger;


// Double check the occurances of this and whatnot, try to fix, etc.
// TODO determine what behavior should happen if UUID does not exist, or username does not exist
@UtilityClass
public class MojangAPI {
    /**
     * When possible, use the cached, faster api at https://api.ashcon.app/mojang/v2/user, otherwise use mojang
     * API URLs
     */

    //private static final @NotNull Gson G = HttpUtil.getDefaultGsonInstance();
    private static final @NotNull OkHttpClient CLIENT = HttpUtil.getDefaultClientInstance();
    private static final LoadingCache<UUID, AshconResponse> responseCache = CacheBuilder.newBuilder()
            .initialCapacity(5)
            .maximumSize(200)
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build(new CacheLoader<UUID, AshconResponse>() {
                @Override
                public AshconResponse load(@NotNull UUID uniqueId) throws HttpException {
                    return getResponse(uniqueId);
                }
            });

    private static final @NotNull HttpUrl COMBINED_API_URL = Objects.requireNonNull(HttpUrl.parse("https://api.ashcon.app/mojang/v2/user/"));
    private static final @NotNull HttpUrl PLAYERDB_MINECRAFT_URL = Objects.requireNonNull(HttpUrl.parse("https://playerdb.co/api/player/minecraft/"));


    private static final HttpUrl MOJANG_STATUS_URL = HttpUrl.parse("https://status.mojang.com/check");
    @NotNull
    private static final HttpUrl MOJANG_API_BASE_URL = HttpUrl.parse("https://api.mojang.com/");

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

    /**
     * Gets the history of usernames that a given {@link UUID} has had, by time
     *
     * @param uniqueId The {@link UUID} of the user to check
     * @return A {@link SortedMap} of the time of each change (With 1970-01-01T00:00:00Z meaning that it was the first Username)
     * @throws HttpException if there is an error with the HTTP Request/Response
     */
    public static @NotNull SortedMap<Instant, String> getUsernameHistory(UUID uniqueId) throws HttpException {
        return getCachedResponse(uniqueId).getUsernameHistory();

    }

    public static byte[] getTexture(@NotNull UUID uniqueId) throws HttpException {
        return getCachedResponse(uniqueId).getTextures().getSkin().getData();
    }

    public static @Nullable String getTextureString(@NotNull UUID uniqueId) throws HttpException {
        return getCachedResponse(uniqueId).getTextureString();
    }

    public static @NotNull String getUsername(UUID uniqueId) throws HttpException {
        return getCachedResponse(uniqueId).getUsername();
    }

    public static @NotNull UUID getUniqueId(String username) throws HttpException {
        HttpUrl url = PLAYERDB_MINECRAFT_URL.resolve(username);
        JsonElement response = HttpUtil.getResponseAsJson(url);
        try {
            return UUID.fromString(
                    response.getAsJsonObject()
                            .get("data").getAsJsonObject()
                            .get("player").getAsJsonObject()
                            .get("meta").getAsJsonObject()
                            .get("id").getAsString());
        } catch (Exception e) {
            throw new HttpException(url, "Invalid JSON received");
        }
    }

    public static @NotNull UUID getUniqueId(@NotNull String username, @NotNull Instant when) throws HttpException {
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
     *
     * @param uniqueId The UUID to get the created time for
     * @return The created time as an instant if possible, or an Instant representing -1 Seconds Unix time if not
     */
    public static @NotNull Instant getCreated(UUID uniqueId) throws HttpException {
        return getCachedResponse(uniqueId).getCreated();
    }

    /**
     * Provides the UUID associated with a given username at a given time.
     * FIXME: This is currently broken, with an <a href="https://bugs.mojang.com/browse/WEB-3367>open bug report at Mojang</a>
     *
     * @param username  The username to check
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

    // Internal Utility funcitons

    private AshconResponse getCachedResponse(UUID uniqueId) throws HttpException{
        try {
            return responseCache.get(uniqueId);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof HttpException)
                throw (HttpException) e.getCause();
            else {
                getLogger().severe("Something went wrong while trying to access a UUID via the cache");
                getLogger().severe(e.getMessage());
                getLogger().severe("Will now try to retrieve the UUID directly");
                return getResponse(uniqueId);
            }
        }
    }


    private AshconResponse getResponse(UUID uniqueId) throws HttpException {
        return HttpUtil.getDefaultGsonInstance().fromJson(HttpUtil.getResponseAsJson(COMBINED_API_URL.resolve(StringUtil.unhyphenateUUID(uniqueId))), AshconResponse.class);
    }
}
