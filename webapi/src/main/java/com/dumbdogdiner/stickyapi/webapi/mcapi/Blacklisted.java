/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.mcapi;

import lombok.Getter;

import java.time.Instant;

@SuppressWarnings("JavaDoc")
public class Blacklisted {
    /**
     * @return If the server is blacklisted
     */
    @Getter
    private boolean blacklisted;
    /**
     * @return Which server was queried
     */
    @Getter
    private String query;
    private String lastUpdate;

    private double took;

    /**
     * @return The last time blacklists were updated
     */
    public Instant getLastUpdate(){
        return Instant.parse(lastUpdate);
    }
}
