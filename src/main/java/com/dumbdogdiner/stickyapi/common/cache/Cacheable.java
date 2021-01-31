/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
/*
<<<<<<< HEAD
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
=======
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
>>>>>>> yeen-cherrypick
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.cache;

import org.jetbrains.annotations.NotNull;

/**
 * Mark a class as being cacheable - must provide the <code>getKey()</code>
 * method to allow for key retrieval.
 */
public interface Cacheable {
    @NotNull String getKey();
}
