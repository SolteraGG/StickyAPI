/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapi.minecraftheads;

import com.dumbdogdiner.stickyapi.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("NotNullFieldNotInitialized")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
/* package-local */ class HeadInfo {
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
     * If the object has any tags defined
     */
    public boolean hasTags(){
        return tags != null;
    }

    /**
     * If the object has tags, return a set of the tags. Otherwise, return an empty set
     * @return A set of the tags, or an empty set
     * @see #hasTags()
     */
    public @NotNull Set<String> getTags() {
        if(this.tags == null)
            return Collections.emptySet();
        return new HashSet<>(Arrays.asList(this.tags.split(",").clone()));
    }
}
