/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.textures.TextureValidator;
import com.dumbdogdiner.stickyapi.webapi.mojang.MojangAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static java.text.MessageFormat.format;

class MinecraftHeadsAPITest {
    @Test
    void getAllHeads() throws HttpException {
        for(HeadInfo info : MinecraftHeadsAPI.getHeads(null)) {
            System.out.println(format("Testing {0}", info.getName()));
            //System.out.println(format("Testing UUID - {0}", info.getUniqueId().toString()));
            //System.out.println(MojangAPI.getUsername(info.getUniqueId()));
            //assertEquals(MojangAPI.getTextureString(info.getUniqueId()), info.getTexture());
            TextureValidator.validateTextureString(info.getTexture());
            

        }
    }


    @Test
    void getTags() {
    }

    @Test
    void testGetTags() {
    }

    @Test
    void getHead() {
    }

    @Test
    void getHeadByTags() {
    }
}