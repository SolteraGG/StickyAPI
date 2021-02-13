/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;


/**
 * A convenience class for converting booleans from {@link JsonPrimitive}s, and for easily creating
 */
public class NbtBooleanTag extends NbtPrimitiveTag<Boolean> {
    private final boolean bool;

    public NbtBooleanTag(boolean bool) {
        this.bool = bool;
    }

    /**
     * Converts a {@link JsonPrimitive} of a boolean to a new {@link NbtBooleanTag}
     * It is recommended to make sure the primitive is a boolean first
     *
     * @param primitive The incoming {@link JsonPrimitive} to be converted, must be a boolean type!
     * @return A new tag with tbe boolean value of the primitive
     * @throws ClassCastException if the primitive is not a boolean
     * @see JsonPrimitive#isBoolean()
     */
    public static @NotNull NbtBooleanTag fromPrimitive(@NotNull JsonPrimitive primitive) throws ClassCastException {
        return new NbtBooleanTag(primitive.getAsBoolean());
    }

    // As much as I hate the object form... we have to because generics
    @Override
    public @NotNull Boolean asPrimitive() {
        return bool;
    }

    @Override
    public @NotNull JsonElement toJson() {
        return new JsonPrimitive(bool);
    }

    @Override
    public @NotNull String toNbtString() {
        return bool ? NbtNumberTag.TRUE.toNbtString() : NbtNumberTag.FALSE.toNbtString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NbtBooleanTag) {
            return ((NbtBooleanTag) other).asPrimitive() == bool;
        } else if (other instanceof NbtNumberTag) {
            return (!((NbtNumberTag) other).asPrimitive().equals(0)) == bool;
        } else {
            return false;
        }
    }
}
