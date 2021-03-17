/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.annotation;

/**
 * Denotes a method, class, or interface that is currently broken. Optionally links to a page about the issue
 */
public @interface Broken {
    public String issuepage() default "";
}
