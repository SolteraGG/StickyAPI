/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.http;

import java.net.URL;

public class HttpException extends Exception {
    public HttpException(URL url) {
        this(url.toExternalForm());
    }

    public HttpException(String url) {
        this(url, "");
    }

    protected HttpException(String str, Exception e) {
        super(str, e);
    }

    HttpException(String url, String err){
        super("An error occurred while accessing " + url + (!err.equals("") ? ": " + err : ""));
    }
}
