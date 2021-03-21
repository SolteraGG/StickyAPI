/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.mcapi;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.time.Instant;

class ServerCache {
    // Internal variables for use by GSON
    private String status;
    @Getter
    private int ttl;
    private String insertion_time;

    private enum CacheStatus {
        HIT, MISS
    }

    public Instant getInsertionTime(){
        Preconditions.checkNotNull(insertion_time);
        return Instant.parse(insertion_time);
    }

    public CacheStatus getCacheStatus(){
        return CacheStatus.valueOf(status);
    }
}
