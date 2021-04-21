/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.misc;

import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.google.gson.JsonElement;
import inet.ipaddr.IPAddress;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Allows checking if we are on Mojang's blacklist
 */
@UtilityClass
public class BlacklistChecks {
    @SuppressWarnings("ConstantConditions")
    private static final @NotNull HttpUrl BLACKLIST_BASE_URL = HttpUrl.parse("https://us.mc-api.net/v3/server/blacklisted/");

    public Instant lastChecked(String hostname) throws HttpException {
        JsonElement response = HttpUtil.getResponseAsJson(BLACKLIST_BASE_URL.resolve(hostname));
        return Instant.parse(response.getAsJsonObject().get("lastUpdate").getAsString());
    }

    public Instant lastChecked(IPAddress ipAddress) throws HttpException {
        Instant altAddrChecked = null;
        if(ipAddress.isIPv6() && ipAddress.isIPv4Convertible()){
            altAddrChecked = lastChecked(ipAddress.toIPv4().toFullString());
        }

        Instant orig = lastChecked(ipAddress.toFullString());

        return (altAddrChecked != null && altAddrChecked.isBefore(orig)) ?
                altAddrChecked : orig;
    }

    public boolean isBlacklisted(String hostname) throws HttpException {
        JsonElement response = HttpUtil.getResponseAsJson(BLACKLIST_BASE_URL.resolve(hostname));
        return response.getAsJsonObject().get("blacklisted").getAsBoolean();
    }

    public boolean isBlacklisted(IPAddress ipAddress) throws HttpException {
        boolean alt = false;
        if(ipAddress.isIPv6() && ipAddress.isIPv4Convertible()){
            alt = isBlacklisted(ipAddress.toIPv4().toFullString());
        }
        return alt || isBlacklisted(ipAddress.toFullString());
    }
}
