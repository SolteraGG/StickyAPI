/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.url;

import lombok.Data;

/**
 * Sub-class for easier URL formatting between methods.
 */
@Data
public class URLPair {
    private String fullPath;
    private String shortened;

    public URLPair(String fullUrl, String shortenedUrl) {
        this.fullPath = fullUrl;
        this.shortened = shortenedUrl;
    }
}
