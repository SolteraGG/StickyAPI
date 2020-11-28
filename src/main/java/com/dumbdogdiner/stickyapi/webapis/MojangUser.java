/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.webapis;

import com.dumbdogdiner.stickyapi.common.user.StickyUser;

import java.util.UUID;

public class MojangUser extends StickyUser {

    public MojangUser(UUID uniqueId) {
        super(uniqueId);
    }
}
