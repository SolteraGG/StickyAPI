/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.namemc;

import lombok.Data;
import lombok.Getter;

import java.awt.Image;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

class ScrapedProfile {
    @Getter
    private String profileId;

    @Getter
    private String currentName;

    private String uuid;
    public UUID getUniqueId(){
        return UUID.fromString(uuid);
    }

    @Getter
    private List<PastName> pastNames;
    private class PastName {
        @Getter
        private String name;
        int changedAt;

        public Instant getChangedAt(){
            return Instant.ofEpochSecond(changedAt);
        }
    }

    @Getter
    private ImageUrls imageUrls;
    @Getter
    private class ImageUrls {
        private String cape;
        private String body;
        private String head;
        private String face;
        private List<String> skins;
    }
}
