/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.cache;

/**
 * Mark a class as being cacheable - must provide the <code>getKey()</code> method to allow
 * for key retrieval.
 */
public interface Cacheable {
    String getKey();
}
