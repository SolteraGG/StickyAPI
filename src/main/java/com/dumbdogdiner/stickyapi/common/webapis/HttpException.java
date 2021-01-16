/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.webapis;

import java.net.URL;

public class HttpException extends RuntimeException {
    public HttpException(URL url) {
        this(url.toExternalForm());
    }

    public HttpException(String url) {
        super("An error occurred while accessing " + url);
    }

    protected HttpException(String str, Exception e) {
        super(str, e);
    }
}
