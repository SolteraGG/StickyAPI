/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */

import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.textures.TextureValidator;
import com.dumbdogdiner.stickyapi.webapi.mojang.MojangAPI;
import com.dumbdogdiner.stickyapi.webapi.mojang.MojangStatus;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MojangAPITest {
    private static final UUID MUMBO = UUID.fromString("ac224782-efff-4296-b08c-dbde8e47abdb");
    private static final UUID MHF_ALEX = UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9");
    private static final UUID RODWUFF = UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9");

    @Test
    void getSkinTexture() throws HttpException {
        assertTrue(TextureValidator.isValidTextureString(MojangAPI.getTextureString(RODWUFF)));
//        System.out.println(new MojangAPI(UUID.fromString("ffffffff-f53b-49d1-b8c4-ffffffffffff")).getSkinTexture());
    }

    @Test
    void getUsernameHistory() throws HttpException {
        Map<Instant, String> response = MojangAPI.getUsernameHistory(UUID.fromString("9b6d27b3-f53b-49d1-b8c4-fa807f7575e9"));
        for(Map.Entry<Instant, String> entry : response.entrySet()){
            String dateStr = entry.getKey() == null ? "" :entry.getKey().toString();
            System.out.println("Name: " + entry.getValue() + "; date: " + dateStr);
        }
    }

    @Test
    void getUsername() throws HttpException {
        assertEquals("MHF_Alex", MojangAPI.getUsername(UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9")));
    }


    @Test
    void getMojangAPIStatus() throws HttpException {
        Map<String, MojangStatus> status = MojangAPI.getMojangAPIStatus();
        assertTrue(status.size() > 0);

        System.out.println("Current status of Mojang APIs:");
        for (Map.Entry<String, MojangStatus>  singleStat: status.entrySet()){
            System.out.println(singleStat.getKey() + ": " + singleStat.getValue().toString());
        }

        assertTrue(status.keySet().containsAll(Arrays.asList(
                "minecraft.net",
                "session.minecraft.net",
                "account.mojang.com",
                "authserver.mojang.com",
                "api.mojang.com",
                "textures.minecraft.net",
                "mojang.com")));
    }
    @Test
    void testUsernameHistory(){

    }
}