/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MojangAPITest {

    @Test
    void getMojangAPIStatus() {
        Map<String, MojangStatus> status = MojangAPI.getMojangAPIStatus();
        assertTrue(status.size() > 0);
        System.out.println("Current status of Mojang APIs:");
        for (Map.Entry<String, MojangStatus>  singleStat: status.entrySet()){
            System.out.println(singleStat.getKey() + ": " + singleStat.getValue().toString());
        }
    }
}