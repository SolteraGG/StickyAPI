/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.arguments;

import org.jetbrains.annotations.NotNull;

/**
 * Argument component for handling actions against an individual argument.
 */
public class ArgumentItem {
    private Arguments parent;
    private String name;

    /**
     * Construct a new argument item for the provided Argument class, with the given argument name.
     * 
     * @param parent Parent Arguments class
     * @param name Name of the individual argument
     */
    public ArgumentItem(@NotNull Arguments parent, @NotNull String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * Set a validation method for the argument.
     * 
     * @param func the Validator function to register
     * @return {@link ArgumentItem}
     */
    public ArgumentItem validator(@NotNull Arguments.Validator func) {
        parent.addValidator(name, func);
        return this;
    }
}
