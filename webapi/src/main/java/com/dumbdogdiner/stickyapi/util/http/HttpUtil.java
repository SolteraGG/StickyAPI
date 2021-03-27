/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.http;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.text.MessageFormat.format;

@UtilityClass
public class HttpUtil {
    @Getter
    private static final OkHttpClient defaultClientInstance = getNewHttpClient();
    @Getter
    private static final Gson defaultGsonInstance = new Gson();

    private static final String USER_AGENT = format("{0}/{1} ({2}) {3}/{4} ({5})",
            "StickyAPI",
            StickyAPI.getVersion(),
            System.getProperty("java.vendor"),
            System.getProperty("java.version"),
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
    );

    public static OkHttpClient getNewHttpClient() {
        return new OkHttpClient.Builder().addNetworkInterceptor(chain ->
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build()))
                .build();
    }

    public static Response getResponse(HttpUrl url) throws HttpException{
        try {
            Response response = defaultClientInstance.newCall(new Request.Builder().url(url).build()).execute();
            if(response.code() != HttpURLConnection.HTTP_OK)
                throw new HttpStatusException(url.url(), response.code());
            if(response.body() == null){
                throw new NullPointerException("The response body was null");
            }
            if(response.body().contentLength() == 0){
                throw new HttpException(url.url(), "No content received");
            }
            if(!response.isSuccessful()){
                throw new HttpException(url.url(), "Response was unsuccessful");
            }
            return response;
        } catch (IOException e){
            throw new HttpConnectionException(url.url(), e);
        } catch (NullPointerException e) {
            throw new HttpException(url.url());
        }
    }

    public static JsonElement getResponseAsJson(HttpUrl url) throws HttpException {
        try {
            Response response = defaultClientInstance.newCall(new Request.Builder().url(url).build()).execute();
            if(response.code() != HttpURLConnection.HTTP_OK)
                throw new HttpStatusException(url.url(), response.code());
            if(response.body().contentLength() == 0){
                throw new HttpException(url.url(), "No content received");
            }
            return JsonParser.parseReader(response.body().charStream());
        } catch (IOException e){
            throw new HttpConnectionException(url.url(), e);
        } catch (NullPointerException e) {
            throw new HttpException(url.url());
        }
    }
}
