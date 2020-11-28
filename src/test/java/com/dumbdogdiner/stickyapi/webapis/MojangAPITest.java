/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapis;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MojangAPITest {

    @Test
    void getSkinTexture() {
        System.out.println(new MojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getSkinTexture());
//        System.out.println(new MojangAPI(UUID.fromString("ffffffff-f53b-49d1-b8c4-ffffffffffff")).getSkinTexture());
    }

    @Test
    void getFullJsonCombinedAPI() {
        System.out.println(new MojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getFullJsonCombinedAPI().toString());
    }

    @Test
    void getUsernameHistory() {
        Map<String, Instant> response = new MojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getUsernameHistory();
        for(String name : response.keySet()){
            String dateStr = response.get(name) == null ? "" : response.get(name).toString();
            System.out.println("Name: " + name + "; date: " + dateStr);
        }
    }
}