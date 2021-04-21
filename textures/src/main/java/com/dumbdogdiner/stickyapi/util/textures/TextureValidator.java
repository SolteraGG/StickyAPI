/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.textures;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.util.http.UrlValidator;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.NullArgumentException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collector;


@UtilityClass
public class TextureValidator {
    /**
     * Byte array containing the file signature of a PNG image
     */
    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 0x89,
            'P', 'N', 'G',
            (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A
    };


    private static final UrlValidator URL_VALIDATOR = UrlValidator.withDomains("*.minecraft.net", "*.mojang.com");
    ;

    /**
     * Validates a URL to make sure it is a valid, loadable Minecraft texture
     *
     * @param textureURL the URL to test
     * @return if the texture is valid
     */
    public static boolean isValidTextureUrl(@NotNull String textureURL) {
        try {
            validateTextureUrl(textureURL);
            return true;
        } catch (InvalidTextureException e) {
            StickyAPI.getLogger().log(Level.INFO, new InvalidTextureException(textureURL, e).getMessage());
            return false;
        }
    }

    /**
     * Validates a URL to make sure it is a valid, loadable Minecraft texture
     *
     * @param textureURL the URL to test
     * @throws InvalidTextureException if the texture is not valid
     */
    public static void validateTextureUrl(@NotNull String textureURL) throws InvalidTextureException {
        try {
            // Make sure URL scheme is valid and that the domain is allowed
            if (URL_VALIDATOR.isValid(textureURL)) {
                throw new MalformedURLException("The texture URL " + textureURL + " specified a domain other than minecraft.net or mojang.com, or is an invalid URL (It does not match the regex of: " + URL_VALIDATOR.getUrlRegex().pattern() + ")");
            }

            // Needs to be resolvable
            @NotNull Response resp = TextureHelper.httpClient.newCall(new Request.Builder().url(textureURL).build()).execute();
            if (resp.code() != 200) {
                resp.close();
                throw new Exception("Non 200 OK Response of " + resp.code() + " received from " + textureURL);
            }

            // Needs to have a content type of PNG
            if (!(resp.header("Content-Type").toLowerCase().startsWith("image/png") || resp.header("Content-Type").toLowerCase().startsWith("binary/octet-stream"))) {
                resp.close();
                throw new Exception("Unexpected Content-Type of " + resp.header("Content-Type") + " was received from " + textureURL);
            }

            // Verify that the image is, in fact, an actual PNG, and a valid PNG
            BufferedImage img;
            try {
                byte[] response = resp.body().bytes();

                if ((img = ImageIO.read(new ByteArrayInputStream(response))) != null) {
                    for (int i = 0; i < PNG_SIGNATURE.length; i++) {
                        if (PNG_SIGNATURE[i] != response[i]) {
                            int j = i + 1;
                            throw new Exception("Byte " + j + " of the response (" + response[i] + ") did not match byte " + j + " of the PNG signature (" + PNG_SIGNATURE[i] + ")");
                        }
                    }

                } else {
                    throw new NullArgumentException("The image retrieved from " + textureURL + " was decoded to null and" /* must not be null */);
                }
            } catch (IOException | NullArgumentException | NullPointerException e) {
                throw new Exception("The content retrieved from " + textureURL + " was not a recognized image, was null, or was decoded to null", e);
            }

            // Needs to be 64x32 or 64x64
            Preconditions.checkArgument(img.getWidth() == 64 && (img.getHeight() == 64 || img.getHeight() == 32),
                    "The texture must be either 64x64 (for a new texture) or 64x32 (for a classic texture)");


        } catch (Exception e) {
            throw new InvalidTextureException(textureURL, e);
        }
    }


    /**
     * Checks a texture string as exhaustively as possible to validate a given String of a Base-64 encoded texture
     *
     * @param texture The string that represents the texture
     * @throws InvalidTextureException if the texture is invalid
     */
    public static void validateTextureString(String texture) throws InvalidTextureException {
        @NotNull String decodedTexture = new String(Base64.getDecoder().decode(texture), StandardCharsets.UTF_8);
        validateTextureJson(JsonParser.parseString(decodedTexture));
    }

    /**
     * Checks a json representation of a texture to make sure it is valid
     *
     * @param json JSON representing the texture
     * @throws InvalidTextureException if the texture is invalid
     */
    public static void validateTextureJson(@NotNull JsonElement json) throws InvalidTextureException {


        validateTextureUrl(TextureHelper.decodeTextureJsonToUrl(json));
    }

    /**
     * Checks a json representation of a texture to make sure it is valid
     *
     * @param json JSON representing the texture
     * @return if the json is valid
     */
    public static boolean isValidTextureJson(@NotNull JsonElement json) {
        try {
            validateTextureJson(json);
            return true;
        } catch (InvalidTextureException e) {
            StickyAPI.getLogger().log(Level.INFO, new InvalidTextureException(json.getAsString(), e).getMessage());
            return false;
        }
    }

    /**
     * Attempts to (as exhaustively as possible) validate a given String of a Base-64 encoded texture
     *
     * @param texture The string that represents the texture
     * @return if the texture is valid or not
     */
    public static boolean isValidTextureString(String texture) {
        try {
            validateTextureString(texture);
            return true;
        } catch (Exception e) {
            StickyAPI.getLogger().log(Level.INFO, new InvalidTextureException(texture, e).getMessage());
            return false;
        }
    }
}
