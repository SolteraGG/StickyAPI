/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * Base class for any NBT tags that wrap simple primitives (and strings)
 *
 * @param <T> The type of primitive
 */
public abstract class NbtPrimitiveTag<T> implements NbtTag {
    /**
     * Gets the original primitive back from the tag
     */
    @NotNull
    public abstract T asPrimitive();
}
