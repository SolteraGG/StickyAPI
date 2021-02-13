/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link NbtTag} that wraps and escapes {@link String}s
 */
public class NbtStringTag extends NbtPrimitiveTag<String> {
    private final String string;
    private final boolean escaped;

    /**
     * Creates a new NbtStringTag from a given string
     * @param string The string to make a tag from
     */
    public NbtStringTag(String string) {
        this.string = string;
        this.escaped = false;
    }

    /**
     * For package-local use only, allows preventing additional string escaping
     * @see #NbtStringTag(String)
     */
    NbtStringTag(String string, boolean escaped){
        this.string = string;
        this.escaped = escaped;
    }


    /**
     * Converts a {@link JsonPrimitive} into a new {@link NbtStringTag}
     *
     * @param primitive The incoming primitive
     * @return a new {@link NbtStringTag} containing the string
     * @throws IllegalArgumentException if the primitive type is wrong
     * @see JsonPrimitive#isString()
     */
    public static NbtStringTag fromPrimitive(JsonPrimitive primitive) throws IllegalArgumentException {
        Preconditions.checkArgument(primitive.isString(), "The primitive must be a string");
        return new NbtStringTag(primitive.getAsString());
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public @NotNull JsonElement toJson() {
        return new JsonPrimitive(string);
    }

    @SuppressWarnings("UnnecessaryStringEscape")
    @Override
    public @NotNull String toSNbt() {
        // Make sure there are no other escapes needed
        return '\'' +
                (escaped ? string : StringUtil.formatChatCodes(string)
                .replace("\'", "\\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n"))
                + '\'';

    }

    @Override
    public @NotNull String asPrimitive() {
        return string;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof NbtStringTag){
            return escaped == ((NbtStringTag) other).escaped && string.equals(((NbtStringTag) other).asPrimitive());
        } else if(other instanceof NbtJsonTag){
            return other.equals(this);
        } else {
            return false;
        }
    }
}
