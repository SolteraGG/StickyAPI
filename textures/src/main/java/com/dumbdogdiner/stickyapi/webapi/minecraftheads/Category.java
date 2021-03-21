/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

/**
 * Categories of head that are supported by the web api
 */
public enum Category {
    ALPHABET, ANIMALS, BLOCKS, DECORATION, FOOD_DRINKS, HUMANS, HUMANOID, MISCELLANEOUS, MONSTERS, PLANTS;

    /**
     * @return Get the category as the web api expects it
     */
    public String toWebString() {
        return toString().toLowerCase().replace("_", "-");
    }
}
