/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.namemc;

import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class NameMC {
    private static final HttpUrl SCRAPE_API_BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("joshm.us.to")
            .addPathSegment("api")
            .addPathSegment("namemc")
            .addPathSegment("v1")
            .build();

    /**
     * Convenience overloaded method for {@link #search(String)}
     *
     * @see #search(String)
     */
    public List<ScrapedProfile> search(UUID query) throws HttpException {
        return search(query.toString());
    }

    public List<ScrapedProfile> search(String query) throws HttpException {
        HttpUrl searchUrl = SCRAPE_API_BASE_URL.newBuilder()
                .addPathSegment("search")
                .addQueryParameter("query", query)
                .build();
        JsonObject response = HttpUtil.getResponseAsJson(searchUrl).getAsJsonObject();

        if (!response.get("success").getAsBoolean())
            throw new HttpException(searchUrl, "API call unsuccessful");

        return collectResults(response.get("results").getAsJsonArray());
    }

    public List<ScrapedProfile> lookupMultipleUUID(List<UUID> queries) throws HttpException {
        return lookupMultiple(queries.stream().map(UUID::toString).collect(Collectors.toList()));
    }

    public List<ScrapedProfile> lookupMultiple(List<String> queries) throws HttpException {
        StringJoiner query = new StringJoiner(",");
        queries.forEach(query::add);
        HttpUrl lookupUrl = SCRAPE_API_BASE_URL.newBuilder()
                .addPathSegment("lookup-multiple")
                .addQueryParameter("query", query.toString())
                .build();

        JsonObject response = HttpUtil.getResponseAsJson(lookupUrl).getAsJsonObject();
        if (!response.get("success").getAsBoolean())
            throw new HttpException(lookupUrl, "API call unsuccessful");

        return collectResults(response.get("results").getAsJsonArray());
    }

    public ScrapedProfile lookup(UUID query) throws HttpException {
        return lookup(query.toString());
    }

    public ScrapedProfile lookup(String query) throws HttpException {
        HttpUrl lookupUrl = SCRAPE_API_BASE_URL.newBuilder()
                .addPathSegment("lookup")
                .addQueryParameter("query", query)
                .build();

        JsonObject response = HttpUtil.getResponseAsJson(lookupUrl).getAsJsonObject();
        if (!response.get("success").getAsBoolean())
            throw new HttpException(lookupUrl, "API call unsuccessful");
        response.remove("success");

        return HttpUtil.getDefaultGsonInstance().fromJson(response, ScrapedProfile.class);
    }

    private List<ScrapedProfile> collectResults(JsonArray jsa){
        ArrayList<ScrapedProfile> profiles = new ArrayList<>();
        jsa.forEach(element -> profiles.add(HttpUtil.getDefaultGsonInstance().fromJson(element, ScrapedProfile.class)));
        return profiles;
    }
}
