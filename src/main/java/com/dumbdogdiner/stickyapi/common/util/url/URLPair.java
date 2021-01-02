/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.url;

/**
 * Sub-class for easier URL formatting between methods.
 */
public class URLPair {
    String fullPath;
    String shortened;

    public URLPair(String fullUrl, String shortenedUrl) {
        this.fullPath = fullUrl;
        this.shortened = shortenedUrl;
    }

    public String getShortened() {
        return shortened;
    }

    public String getFullPath() {
        return fullPath;
    }

}
