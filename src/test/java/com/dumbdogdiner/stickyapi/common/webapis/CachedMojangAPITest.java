/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.common.util.textures.TextureValidator;
import com.dumbdogdiner.stickyapi.common.webapis.mojang.CachedMojangAPI;
import com.dumbdogdiner.stickyapi.common.webapis.mojang.MojangStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CachedMojangAPITest {
    @Test
    void getSkinTexture() {
        assertTrue(TextureValidator.isValidTextureString(CachedMojangAPI.getTextureString(UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9"))));
//        System.out.println(new MojangAPI(UUID.fromString("ffffffff-f53b-49d1-b8c4-ffffffffffff")).getSkinTexture());
    }

    @Test
    void getFullJsonCombinedAPI() {
        System.out.println(CachedMojangAPI.getJsonResponse(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9")).toString());
    }

    @Test
    void getUsernameHistory() {
        Map<Instant, String> response = CachedMojangAPI.getUsernameHistory(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9"));
        for(Map.Entry<Instant, String> entry : response.entrySet()){
            String dateStr = entry.getKey() == null ? "" :entry.getKey().toString();
            System.out.println("Name: " + entry.getValue() + "; date: " + dateStr);
        }
    }

    @Test
    void getUsername() {


        assertEquals("MHF_Alex", CachedMojangAPI.getUsername(UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9")));
    }


    @Test
    void getMojangAPIStatus() {
        Map<String, MojangStatus> status = CachedMojangAPI.getMojangAPIStatus();
        assertTrue(status.size() > 0);



        System.out.println("Current status of Mojang APIs:");
        for (Map.Entry<String, MojangStatus>  singleStat: status.entrySet()){
            System.out.println(singleStat.getKey() + ": " + singleStat.getValue().toString());
        }
    }
}