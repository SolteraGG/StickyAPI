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
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Cat;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class MinecraftHeadsAPI {
    private static final HttpUrl APIURL = HttpUrl.parse("https://minecraft-heads.com/scripts/api.php");/* new HttpUrl.Builder()
            .scheme("https")
            .host("minecraft-heads.com")
            .addPathSegments("scripts/api.php")
            .build();*/



    private final static Map<Category, Map<String /* name */, HeadInfo>> heads = new HashMap<>();
    static {
        for (Category c : Category.values()){
            try {
                heads.put(c, update(c));
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Category {
        ALPHABET, ANIMALS, BLOCKS, DECORATION, FOOD_DRINKS, HUMANS, HUMANOID, MISCELLANEOUS, MONSTERS, PLANTS;
        @Override
        public String toString(){
            return super.toString().toLowerCase().replace("_", "-");
        }
    }


    private static Request buildRequest(Category cat) {
        assert APIURL != null;
        return new Request.Builder().url(APIURL.newBuilder()
                .addQueryParameter("cat", cat.toString())
                .addQueryParameter("tags", Boolean.TRUE.toString())
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

    public static Set<String> getTags(Category category) throws HttpException {
        Set<String> tagsSet = new HashSet<>();
        heads.get(category).values().stream().map(HeadInfo::getTags).collect(Collectors.toSet()).forEach(tagsSet::addAll);
        return tagsSet;
    }

    /**
     *
     * @param cat Which category to return the heads for; If category is Null, it gives all heads, of all categories
     * @return
     */
    public static Set<HeadInfo> getHeads(@Nullable Category cat) {

        if(cat == null) {
            Set<HeadInfo> head = new HashSet<>();
            for(Category c : Category.values()){
                head.addAll(getHeads(c));
            }
            return head;
        }
        return new HashSet<>(heads.get(cat).values());
    }

    public static HeadInfo getHead(Category cat, String name){
        return heads.get(cat).get(name);
    }

    public static Set<HeadInfo> getHeadByTags(@Nullable Category c, String name){
        Set<HeadInfo> head = new HashSet<>();
        if(c == null) {
            for(Category cat : Category.values())
                head.addAll(getHeadByTags(cat, name));
        } else {
            heads.get(c).values().forEach(headInfo -> headInfo.getTags().forEach(s -> {
                if (s.equalsIgnoreCase(name))
                    head.add(headInfo);
            }));
        }
        return head;
    }

    public static void updateHeads() throws HttpException {
        for(Category c : Category.values()){
            updateHeads(c);
        }
    }

    public static void updateHeads(Category c) throws HttpException {

    }

    private static Map<String, HeadInfo> update(Category c) throws HttpException {
        HashMap<String, HeadInfo> headInfoCategory = new HashMap<>();
        Request request = buildRequest(c);
        try {
            Response r = HttpUtil.getDefaultClientInstance().newCall(request).execute();
            JsonElement resp = JsonParser.parseReader(r.body().charStream());
            assert resp.isJsonArray();
            for(JsonElement el : resp.getAsJsonArray()){
                HeadInfo info = HttpUtil.getDefaultGsonInstance().fromJson(el, HeadInfo.class);
                headInfoCategory.put(info.getName(), info);
            }
            return headInfoCategory;
        } catch (IOException e) {
            throw new HttpConnectionException(request, e);
        } catch (NullPointerException e){
            throw new HttpException("Received null body", e);
        }
    }
}
