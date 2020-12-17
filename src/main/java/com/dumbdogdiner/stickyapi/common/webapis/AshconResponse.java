/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import java.util.List;

public class AshconResponse {
    String uuid;
    String username;

    List<Username> username_history;

    class Username {
        String username;
        String changed_at;


        Username(String username, String changed_at) {
            this.username = username;
            this.changed_at = changed_at;
        }
    }


    Textures textures;
    class Textures {
        boolean custom;
        boolean slim;
        Skin skin;

        class Skin{
            String url;
            String data;
        }

        Raw raw;

        class Raw{
            String value;
            String signature;
        }
    }
    String created_at;
}
