/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.common.util.textures.TextureHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Inner utility class to easily convert the received object from the Cached API to a java object.
 */

class AshconResponse {
    String uuid = new UUID(0,0).toString();
    String username = "Steve";

    List<Username> username_history = new ArrayList<>();

    static class Username {
        String username;
        String changed_at;


        Username(String username, String changed_at) {
            this.username = username;
            this.changed_at = changed_at;
        }
    }


    Textures textures;
    static class Textures {
        boolean custom;
        boolean slim;
        Skin skin;

        static class Skin{
            String url;
            String data;
        }

        Raw raw;

        static class Raw{
            String value = TextureHelper.getTexture("MHF.MHF_Steve");
            String signature;
        }
    }
    String created_at;
}
