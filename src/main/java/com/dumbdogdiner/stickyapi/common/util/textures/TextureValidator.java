/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.textures;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.validator.routines.UrlValidator;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;

@UtilityClass
public class TextureValidator {
    /**
     * Byte array containing the file signature of a PNG image
     */
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a};

    /**
     *
     * @param url
     * @return
     */
    public static boolean isValidTextureUrl(String url) {
        //TODO implement
        return true;
    }

    /**
     * Attempts to (as exhaustively as possible) validate a given String of a Base-64 encoded texture
     *
     * @param texture The string that represents the texture
     * @return if the texture is valid or not
     */
    public static boolean isValidTexture(String texture) {
        try {
            String decodedTexture = new String(Base64.getDecoder().decode(texture), StandardCharsets.UTF_8);
            String textureURL = JsonParser.parseString(decodedTexture)
                    .getAsJsonObject().get("textures")
                    .getAsJsonObject().get("SKIN")
                    .getAsJsonObject().get("url")
                    .getAsString();
            if (!UrlValidator.getInstance().isValid(textureURL)) {
                throw new MalformedURLException("The texture URL " + textureURL + " is not a valid URL");
            }
            String host = HttpUrl.get(textureURL).host().toLowerCase();
            if (!(host.endsWith("mojang.com") || host.endsWith("minecraft.net"))) {
                throw new MalformedURLException("The texture URL " + textureURL + " specified a host other than minecraft.net or mojang.com");
            }
            Response resp = TextureHelper.httpClient.newCall(new Request.Builder().url(textureURL).build()).execute();
            if (resp.code() != 200) {
                resp.close();
                throw new Exception("Non 200/OK Response of " + resp.code() + " received from " + textureURL);
            }
            if (!"image/png".equalsIgnoreCase(resp.header("Content-Type"))) {
                resp.close();
                throw new Exception("Unexpected format of " + resp.header("Content-Type") + " was received from " + textureURL);
            }
            try {
                byte[] response = resp.body().bytes();
                if (ImageIO.read(new ByteArrayInputStream(response)) != null) {
                    for (int i = 0; i < PNG_SIGNATURE.length; i++) {
                        if (PNG_SIGNATURE[i] != response[i]) {
                            int j = i + 1;
                            throw new Exception("Byte " + j + " of the response (" + response[i] + ") did not match byte " + j + " of the PNG signature (" + PNG_SIGNATURE[i] + ")");
                        }
                    }
                    return true;
                } else {
                    throw new NullArgumentException("The image retrieved from " + textureURL + " was decoded to null and" /* must not be null */);
                }
            } catch (IOException | NullArgumentException | NullPointerException e) {
                throw new Exception("The content retrieved from " + textureURL + " was not a recognized image, was null, or was decoded to null", e);
            }
        } catch (Exception e) {
            StickyAPI.getLogger().log(Level.INFO, new InvalidTextureException(texture, e).getMessage());
            return false;
        }
    }


}
