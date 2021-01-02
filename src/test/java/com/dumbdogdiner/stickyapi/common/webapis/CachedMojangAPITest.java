/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CachedMojangAPITest {
    @Test
    void getSkinTexture() {
        assertTrue(TextureHelper.validateTexture(CachedMojangAPI.getSkinTexture(UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9"))));
//        System.out.println(new MojangAPI(UUID.fromString("ffffffff-f53b-49d1-b8c4-ffffffffffff")).getSkinTexture());
    }

    @Test
    void getFullJsonCombinedAPI() {
        System.out.println(new CachedMojangAPI(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).getFullJsonCombinedAPI().toString());
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


        assertEquals("MHF_Alex", new CachedMojangAPI(UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9")).getUsername());
    }
}