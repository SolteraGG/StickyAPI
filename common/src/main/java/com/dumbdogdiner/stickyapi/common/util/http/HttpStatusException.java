/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.http;

import okhttp3.Response;

import java.net.URL;

public class HttpStatusException extends HttpException {
    public HttpStatusException(URL url, int responseCode) {
        this(url.toExternalForm(), responseCode);
    }

    public HttpStatusException(String url, int responseCode) {
        super("An error occurred while accessing " + url + "; Response code of " + responseCode + " received.");
    }

    public HttpStatusException(URL url, int responseCode, int expectedCode) {
        this(url.toExternalForm(), responseCode, expectedCode);
    }

    public HttpStatusException(String url, int responseCode, int expectedCode) {
        super("An error occurred while accessing " + url + "; Response code of " + responseCode + " received, expected " + expectedCode);
    }

    public HttpStatusException(Response resp) {
        this(resp.request().url().toString(), resp.code());
    }
}
