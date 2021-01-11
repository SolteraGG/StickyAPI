/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

public class MojangUser extends StickyUser {
    private final AshconResponse apiResponse;
    public MojangUser(@NotNull UUID uniqueId) {
        super(uniqueId);
        apiResponse = new CachedMojangAPI(uniqueId).getResponse();
        name = apiResponse.username;
    }

    public MojangUser(@NotNull StickyUser p) {
        super(p);
        apiResponse = new CachedMojangAPI(uniqueId).getResponse();
        name = apiResponse.username;
    }


    public String getTextureRawValue(){
        return apiResponse.textures.raw.value;
    }

    public @NotNull Map<String, List<Instant>> getUsernameHistory(){
        @NotNull HashMap<String, List<Instant>> usernameHistory = new HashMap<>();
        for (AshconResponse.@NotNull Username u : apiResponse.username_history) {
            if(usernameHistory.containsKey(u.username) && u.changed_at != null){
                usernameHistory.get(u.username).add(Instant.parse(u.changed_at));
            } else {
                @NotNull ArrayList<Instant> hist = new ArrayList<>();
                if(u.changed_at != null){
                    hist.add(Instant.parse(u.changed_at));
                }
                usernameHistory.put(u.username, hist);
            }
        }
        return usernameHistory;
    }
}
