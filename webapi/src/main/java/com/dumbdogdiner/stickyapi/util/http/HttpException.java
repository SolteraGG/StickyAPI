/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.http;

import okhttp3.HttpUrl;

import java.net.URL;

public class HttpException extends Exception {
    public HttpException(URL url) {
        this(url.toExternalForm());
    }

    public HttpException(String url) {
        super("An error occurred while accessing " + url);
    }

    public HttpException(HttpUrl url, String error){
        this(url.url(), error);
    }

    HttpException(URL url, String error){
        super("An error occurred while accessing " + url.toExternalForm() + ": " + error);
    }

    public HttpException(String str, Exception e) {
        super(str, e);
    }
}
