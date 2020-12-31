/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.user;

import com.dumbdogdiner.stickyapi.common.cache.Cacheable;
import lombok.Getter;

import java.util.UUID;

public class StickyUser implements Cacheable {
    @Getter
    protected UUID uniqueId;

    @Getter
    protected String name;

    public StickyUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.name = null;
    }

    public StickyUser(StickyUser p){
        uniqueId = p.getUniqueId();
        name = p.getName();
    }

    /**
     * For use
     * @param uniqueId
     * @param userName
     */
    protected StickyUser(UUID uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.name = userName;
    }

    @Override
    public String getKey() {
        return uniqueId.toString();
    }
}
