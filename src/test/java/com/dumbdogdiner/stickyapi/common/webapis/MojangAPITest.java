/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CachedMojangAPITest {

    @Test
    void getSkinTexture() {
        System.out.println(new CachedMojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getSkinTexture());
//        System.out.println(new MojangAPI(UUID.fromString("ffffffff-f53b-49d1-b8c4-ffffffffffff")).getSkinTexture());
    }

    @Test
    void getFullJsonCombinedAPI() {
        System.out.println(new MojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getFullJsonCombinedAPI().toString());
    }

    @Test
    void getUsernameHistory() {
        Map<String, Instant> response = new CachedMojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getUsernameHistory();
        for(String name : response.keySet()){
            String dateStr = response.get(name) == null ? "" : response.get(name).toString();
            System.out.println("Name: " + name + "; date: " + dateStr);
        }
    }

    @Test
    void getUsername() {
        assertEquals(new CachedMojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getUsername(), "Rodwuff");
    }
}