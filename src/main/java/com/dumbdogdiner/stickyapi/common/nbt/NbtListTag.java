/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A wrapper class that allows the construction of or conversion (from JSON) of NBT List Tags
 */

public class NbtListTag extends ArrayList<NbtTag> implements NbtTag  {
    /**
     * Generates a new, empty {@link NbtListTag}
     * {@inheritDoc}
     */
    public NbtListTag() {
        super();
    }

    /**
     * Generate a new {@link NbtListTag} with elements from an existing array or individual objects
     */
    public NbtListTag(NbtTag ... tags){
        super(Arrays.asList(tags));
    }

    /**
     * Converts a JSON array into an NBT List tag of the native types
     * @param arr The Json Array to convert
     * @return the new {@link NbtListTag}
     */
    public static NbtListTag fromJsonArray(JsonArray arr){
        NbtListTag listTag = new NbtListTag();
        for(JsonElement jse : arr){
            listTag.add(NbtJsonAdapter.jsonToNbt(jse));
        }
        return listTag;
    }

    /**
     * Creates an NBT tag that is a list of stringified JSON elements from source array
     * @param arr Source JSON array to convert
     * @return The newly converted NBT list tag
     */
    public static NbtListTag fromJsonArrayQuoted(JsonArray arr){
        NbtListTag listTag = new NbtListTag();
        for(JsonElement jse : arr){
            listTag.add(new NbtJsonTag(jse));
        }
        return listTag;
    }

    @Override
    public @NotNull JsonElement toJson() {
        JsonArray array = new JsonArray();
        for(NbtTag tag : this){
            array.add(tag.toJson());
        }
        return array;
    }

    @Override
    public @NotNull String toNbtString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        forEach(nbtTag -> joiner.add(nbtTag.toNbtString()));
        return joiner.toString();
    }

    /**
     * Creates a {@link NbtStringTag} of escaped NBT as a string
     */
    public NbtStringTag toNbtStringTag() {
         return new NbtStringTag(toNbtString(), true);
    }
}
