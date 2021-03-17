/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

import com.dumbdogdiner.stickyapi.util.http.HttpConnectionException;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.http.HttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// TODO WIP
@UtilityClass
public class MinecraftHeadsAPI {
    private static Map<Category, Map<String /* name */, HeadInfo>> heads;

    public enum Category {
        ALPHABET, ANIMALS, BLOCKS, DECORATION, FOOD_DRINKS, HUMANS, HUMANOID, MISCELLANEOUS, MONSTERS, PLANTS;
        @Override
        public String toString(){
            return super.toString().toLowerCase().replace("_", "-");
        }
    }

    private static final HttpUrl APIURL = new HttpUrl.Builder()
            .scheme("https")
            .host("minecraft-heads.com")
            .addPathSegment("scripts")
            .addPathSegment("api.php")
            .build();

    private static Request buildRequest(Category cat, boolean tags) {
        return new Request.Builder().url(APIURL.newBuilder()
                .addQueryParameter("cat", cat.toString())
                .addQueryParameter("tags", String.valueOf(tags))
                .build())
                .build();
    }

    public static Set<String> getTags() throws HttpException {
        Set<String> tags = new HashSet<>();

        for(Category c : Category.values()){
            tags.addAll(getTags(c));
        }
        return tags;
    }

    public Set<String> getTags(Category category) throws HttpException {
        Set<String> tagsSet = new HashSet<>();
        heads.get(category).values().stream().map(HeadInfo::getTags).collect(Collectors.toSet()).forEach(tagsSet::addAll);
        return tagsSet;
    }

    public HeadInfo getHead(Category cat, String name){
        return heads.get(cat).get(name);
    }

    public String getTexture(Category cat, String name){
        return heads.get(cat).get(name).getTexture();
    }

    public Set<HeadInfo> getHeadByTags(@Nullable Category c, String name){
        Set<HeadInfo> head =
    }

    public static  void updateHeads() throws HttpException {
        for(Category c : Category.values()){
            updateHeads(c);
        }
    }

    public static void updateHeads(Category c) throws HttpException {
        HashMap<String, HeadInfo> headInfoCategory = new HashMap<>();
        Request request = buildRequest(c, true);
        try {
            Response r = HttpUtil.getDefaultClientInstance().newCall(request).execute();
            JsonElement resp = JsonParser.parseReader(r.body().charStream());
            assert resp.isJsonArray();
            for(JsonElement el : resp.getAsJsonArray()){
                HeadInfo info = HttpUtil.getDefaultGsonInstance().fromJson(el, HeadInfo.class);
                headInfoCategory.put(info.getName(), info);
            }
            heads.put(c, headInfoCategory);
        } catch (IOException e) {
            throw new HttpConnectionException(request, e);
        } catch (NullPointerException e){
            throw new HttpException("Received null body", e);
        }
    }
}
