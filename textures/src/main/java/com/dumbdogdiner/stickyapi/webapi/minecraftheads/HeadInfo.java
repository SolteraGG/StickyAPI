/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Helper object to hold all the stuff returned by the head api
 */
@SuppressWarnings("FieldMayBeFinal")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class HeadInfo {
    @Getter
    private @NotNull String name;
    private @NotNull String uuid;
    private @NotNull String value;
    private @Nullable String tags;

    /**
     * @return The UUID that minecraft-heads.com has specified as the head
     */
    public @NotNull UUID getUniqueId() {
        return StringUtil.hyphenateUUID(uuid);
    }

    public @NotNull String getTexture() {
        return value;
    }

    /**
     * @return If the given head has any tags
     */
    public boolean hasTags(){
        return tags != null && tags.length() > 0;
    }

    public Set<String> getTags(){
        Preconditions.checkNotNull(tags);
        return new HashSet<>(Arrays.asList(tags.split(",")));
    }
}
