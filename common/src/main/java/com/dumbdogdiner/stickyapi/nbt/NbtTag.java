/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.nbt;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public interface NbtTag {
    /**
     * Converts the {@link NbtTag} back to a {@link JsonElement}
     * @return a {@link JsonElement} of type equivalent to the source tag
     */
    @NotNull
    JsonElement toJson();

    /**
     * Converts a tag into <a href="https://minecraft.gamepedia.com/NBT_format">Stringified NBT</a>
     * @return A representation of the object as Stringified NBT
     */
    @NotNull
    String toNbtString();
}
