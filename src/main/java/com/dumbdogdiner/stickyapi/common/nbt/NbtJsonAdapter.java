/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * Utility class for easy creation of the appropriate {@link NbtTag} from an existing {@link JsonElement}
 */
@UtilityClass
public final class NbtJsonAdapter {
    /**
     * Creates the appropriate tag from a given {@link JsonElement}, but never creates a {@link NbtJsonTag}
     * @param jse The incoming {@link JsonElement} to convert
     * @return the new {@link NbtTag}
     */
    public static NbtTag jsonToNbt(@NotNull JsonElement jse) {
        if(jse.isJsonArray()){
            return NbtListTag.fromJsonArray(jse.getAsJsonArray());
        } else if(jse.isJsonObject()){
            return NbtCompoundTag.fromJsonObject(jse.getAsJsonObject());
        } else if(jse.isJsonPrimitive()){
            JsonPrimitive primitive = jse.getAsJsonPrimitive();
            if(primitive.isBoolean()){
                return NbtBooleanTag.fromPrimitive(primitive);
            } else if(primitive.isNumber()){
                return NbtNumberTag.fromPrimitive(primitive);
            } else if(primitive.isString()){
                return NbtStringTag.fromPrimitive(primitive);
            } else {
                throw new UnsupportedOperationException("Illegal type of NBT primitive");
            }
        } else if(jse.isJsonNull()) {
            return null;
        } else {
            throw new UnsupportedOperationException(MessageFormat.format("This type of JSONElement is unsupported ({0})", jse.getClass().getName()));
        }

    }
}
