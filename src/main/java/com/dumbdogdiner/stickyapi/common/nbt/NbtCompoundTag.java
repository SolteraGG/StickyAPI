/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A wrapper tag that allows working with Compound NBT tags as maps!
 */
public class NbtCompoundTag extends HashMap<String, NbtTag> implements NbtTag {

    /**
     * Creates a new {@link NbtCompoundTag} from an existing {@link Map}
     * {@inheritDoc}
     */
    public NbtCompoundTag(Map<String, NbtTag> map) {
        super(map);
    }

    /**
     * Create a new, blank {@link NbtCompoundTag}
     *
     * {@inheritDoc}
     */
    public NbtCompoundTag() {
        super();
    }

    /**
     * Create a new {@link NbtCompoundTag} with an existing key and tag (useful for 1-element compound tags!)
     * @param key The key to set
     * @param tag The tag to assign
     */
    public NbtCompoundTag(String key, NbtTag tag){
        super();
        put(key, tag);
    }

    /**
     * Constructs a new {@link NbtCompoundTag} from a given {@link JsonObject}
     */
    public static NbtCompoundTag fromJsonObject(JsonObject object){
        NbtCompoundTag tag = new NbtCompoundTag();
        for(String elementName : object.keySet()){
            tag.put(elementName, NbtJsonAdapter.fromJson(object.get(elementName)));
        }
        return tag;
    }

    @Override
    public @NotNull JsonElement toJson() {
        JsonObject object = new JsonObject();
        forEach((name, nbtTag) -> {
            if(nbtTag instanceof NbtPrimitiveTag){
                Object primitive = ((NbtPrimitiveTag<?>) nbtTag).asPrimitive();
                if(primitive instanceof  NbtNumberTag){
                    object.addProperty(name, ((NbtNumberTag) primitive).asPrimitive());
                } else if(primitive instanceof NbtBooleanTag){
                    object.addProperty(name, ((NbtBooleanTag) primitive).asPrimitive());
                } else if(primitive instanceof NbtStringTag){
                    object.addProperty(name, ((NbtStringTag) primitive).asPrimitive());
                }
            } else {
                object.add(name, nbtTag.toJson());
            }
        });
        return object;
    }

    @Override
    public @NotNull String toNbtString() {
        StringJoiner stringNbt = new StringJoiner(",", "{", "}");
        forEach((name, nbtTag) -> {
            StringJoiner element = new StringJoiner(":");
            element.add(name);
            element.add(nbtTag.toNbtString());
            stringNbt.add(element.toString());
        });
        return stringNbt.toString();
    }

    /**
     * Convenience method to add a number directly to the compound tag with a given key
     * @return The original number
     */
    public @NotNull Number put(@NotNull String key, @NotNull Number n) {
        put(key, new NbtNumberTag(n));
        return n;
    }

    /**
     * Convenience method to add a boolean directly to the compound tag with a given key
     * @return The original boolean
     */
    public boolean put(@NotNull String key, boolean b) {
        put(key, new NbtBooleanTag(b));
        return b;
    }

    /**
     * Convenience method to add a String directly to the compound tag with a given key
     * @return The original String
     */
    public String put(@NotNull String key, @NotNull String s){
        put(key, new NbtStringTag(s));
        return s;
    }

}
