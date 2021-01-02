/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.common.cache.Cacheable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StickyUser implements Cacheable {
    @Getter
    protected UUID uniqueId;

    @Getter
    protected String name;

    /**
     * The normal/default constructor for a {@link StickyUser}
     * @param uniqueId the {@link UUID} for the user
     */
    public StickyUser(@NotNull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.name = null;
    }

    /**
     * Create a {@link StickyUser} from another {@link StickyUser}
     * @param stickyUser the {@link StickyUser} to convert to this type of {@link StickyUser}
     */
    public StickyUser(@NotNull StickyUser stickyUser){
        uniqueId = stickyUser.getUniqueId();
        name = stickyUser.getName();
    }

    /**
     * For use by extending classes only
     * @param uniqueId the {@link UUID} of the user
     * @param userName the name of the user
     */
    protected StickyUser(@NotNull UUID uniqueId, @NotNull String userName) {
        this.uniqueId = uniqueId;
        this.name = userName;
    }

    @Override
    public @NotNull String getKey() {
        return uniqueId.toString();
    }
}
