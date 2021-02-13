/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.nbt;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A wrapper class that allows you to deal with numbers in NBT
 */
public class NbtNumberTag extends NbtPrimitiveTag<Number> {
    private final @NotNull Number number;

    // AFIK, Minecraft numbers are always in US type format, I.E. US style decimal separator
    public static final @NotNull NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##########", DecimalFormatSymbols.getInstance(Locale.US));
    public static final @NotNull NbtNumberTag TRUE = new NbtNumberTag(1);
    public static final @NotNull NbtNumberTag FALSE = new NbtNumberTag(0);

    /**
     * Create a new {@link NbtNumberTag} from an existing {@link Number}
     * Warning: this may sometimes produce unintended consequences if the type of number is not supported by minecraft
     * @param number the number to wrap
     */
    public NbtNumberTag(@NotNull Number number) {
        Preconditions.checkNotNull(number);
        this.number = number;
    }

    /**
     * Converts a numerical {@link JsonPrimitive} to the {@link NbtNumberTag} wrapper type
     * @param primitive the incoming primative to convert
     * @return The newly created {@link NbtNumberTag}
     */
    public static NbtNumberTag fromPrimitive(@NotNull JsonPrimitive primitive){
        Preconditions.checkNotNull(primitive);
        Preconditions.checkArgument(primitive.isNumber(), "The primative must be a number");
        return new NbtNumberTag(primitive.getAsNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Number asPrimitive() {
        return number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonElement toJson() {
        return new JsonPrimitive(number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toNbtString() {
        return NUMBER_FORMAT.format(number);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof NbtNumberTag){
            return number.equals(((NbtNumberTag) other).asPrimitive());
        } else if(other instanceof NbtBooleanTag){
            return other.equals(this);
        } else {
            return false;
        }
    }
}
