/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis.mojang;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/**
 * Utility class to easily convert the received object from the Cached API to a java object.
 */

class AshconResponse {
    protected String uuid;


    @Getter
    protected String username;


    protected final @NotNull List<Username> username_history = new ArrayList<>();

    public String getTextureString() {
        return textures.raw.value;
    }

    private static class Username {
        @Getter
        private String username;
        private String changed_at;

        private @NotNull Instant getChangedAt() {
            if(changed_at == null) {
                // Happens for the very very first username, just set to 0 I guess?
                return Instant.ofEpochMilli(0L);
            } else {
                return Instant.parse(changed_at);
            }
        }


        @Override
        public String toString(){
            return username;
        }
    }

    @Getter
    private Texture textures;
    static class Texture {
        @Getter
        private boolean custom;
        @Getter
        private boolean slim;
        @Getter
        private Skin skin;

        static class Skin {
            private String url;
            @SneakyThrows
            public URL getUrl(){
                // The URL should always be valid!
                return new URL(url);
            }
            private String data;

            public byte [] getData(){
                return Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
            }
        }

        @Getter
        Raw raw;

        static class Raw{
            @Getter
            private String value;
            @Getter
            private String signature;
        }
    }

    private String created_at;
    public @Nullable Instant getCreated(){
        if(created_at == null)
            return null;
        return Instant.parse(created_at);
    }

    public @NotNull UUID getUniqueId(){
        return UUID.fromString(uuid);
    }

    public @NotNull SortedMap<Instant, String> getUsernameHistory(){
        TreeMap<Instant, String> usernameHistory = new TreeMap<>(Instant::compareTo);
        for(Username username : username_history) {
            usernameHistory.put(username.getChangedAt(), username.getUsername());
        }
        return usernameHistory;
    }

}
