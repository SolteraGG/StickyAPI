/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.http;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;
import static java.text.MessageFormat.format;

public class HttpClient {
    @Getter
    private final OkHttpClient httpClient;
    private static final String USER_AGENT = format("{0}/{1} ({2}) {3}/{4} ({5})",
            "StickyAPI",
            StickyAPI.getVersion(),
            System.getProperty("java.vendor"),
            System.getProperty("java.version"),
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
            );
    public HttpClient(){
        httpClient = new OkHttpClient.Builder().addNetworkInterceptor(chain ->
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build()))
                .build();
    }

    public JsonElement getResponseAsJson(HttpUrl url) throws HttpException {
        try {
            Response response = httpClient.newCall(new Request.Builder().url(url).build()).execute();
            if(response.code() != HttpURLConnection.HTTP_OK)
                throw new HttpStatusException(url.url(), response.code());
            if(response.body().contentLength() == 0){
                throw new HttpException(url.url().toExternalForm(), "No content received");
            }
            return JsonParser.parseReader(response.body().charStream());
        } catch (IOException e){
            throw new HttpConnectionException(url.url(), e);
        } catch (NullPointerException e) {
            throw new HttpException(url.url());
        }
    }
}
