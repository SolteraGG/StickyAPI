/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

/**
 * A special type of {@link NbtTag} that converts an arbitrary {@link JsonElement} into an appropriately escaped string as NBT
 */
public class NbtJsonTag implements NbtTag{
    private final @NotNull JsonElement element;
    private static final Gson G = new GsonBuilder()
            // Make sure things aren't weirdly escaped, may need to turn this back on
            .disableHtmlEscaping()
            .create();

    public NbtJsonTag(@NotNull JsonElement element) {
        this.element = element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonElement toJson() {
        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toNbtString() {
        return '\'' + G.toJson(element)
                // Because minecraft json is a hack on a hack.....
                // And sometimes the NBT is just json that gets quoted

                .replaceAll("(?<!\\\\)[']", "\\\\'") // Escape single quotes
                // FIXME: do we need to escape double quotes too?? In theory we don't as long as we are using a single quote style!
                .replace("\n", "\\n") // Replace new lines with escaped versions
                .replaceAll("\\+n", "\\\\n") // Fix formatting of any escaped new lines already in there
            +'\'';
    }

    @Override
    public boolean equals(Object other){
        return ((other instanceof NbtJsonTag || other instanceof NbtStringTag) && toNbtString().equals(((NbtTag) other).toNbtString()));
    }
}
