/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.http;

import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.net.URL;

/**
 * Runetime exception to wrap IOExceptions from HTTP requests
 */
public class HttpConnectionException extends HttpException {
    public HttpConnectionException(URL url, IOException e){
        this(url.toExternalForm(), e);
    }

    public HttpConnectionException(String url, IOException e) {
        super("The request to " + url + " failed", e);
    }

    public HttpConnectionException(Request request, IOException e) {
        this(request.url().toString(), e);
    }

    public HttpConnectionException(HttpUrl url, IOException e){
        this(url.toString(), e);
    }
}
