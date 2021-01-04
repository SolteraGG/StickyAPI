/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.textures;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.validator.routines.UrlValidator;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

/**
 * Contains various helper methods related to Textures, Texture Strings, and texture validation, as well as providing
 * access to the bundled texture resources
 */
//TODO write javadoc
@UtilityClass
public class TextureHelper {
    /**
     * Byte array containing the file signature of a PNG image
     */
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a};
    private static final OkHttpClient httpClient = new OkHttpClient();

    private static Map<String, Map<String, String>> TextureMap;
    private static final Yaml YAML = new Yaml();
    private static final Gson GSON = new Gson();

    static {

        try (InputStream test = StickyAPI.getResourceAsStream("/textures.yml")) {
            TextureMap = YAML.load(test);
            //System.out.println(a.getClass());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An unknown error occurred while accessing the builtin resource 'textures.yml'.", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("The integrated textures.yml resource was invalid. Please check the format at compile-time. If you are a server owner, contact the developers of StickyAPI", e);
        }
    }

    /**
     * Gets the set of categories of textures in the Heads file
     */
    public static Set<String> getCategories() {
        return TextureMap.keySet();
    }


    public static Map<String, String> getTextureMapCategory(String cat) {
        return TextureMap.get(cat.toUpperCase());
    }

    public static String getTexture(String cat, String name) {
        return TextureMap.get(cat.toUpperCase()).get(name.toUpperCase());
    }

    public static String getTexture(String qualifiedName) {
        String[] splits = qualifiedName.split("\\.");
        if (splits.length != 2 && splits.length != 1)
            throw new RuntimeException("Invalid qualified name: " + qualifiedName);
        if (splits[1].equals("*")) {
            String texture = null;
            for (String cat : getCategories()) {
                try {
                    texture = getTexture(cat, qualifiedName);
                } catch (NoSuchElementException e) {
                    continue;
                }
                return texture;
            }
            return texture;
        } else {
            return getTexture(splits[0], splits[1]);
        }
    }

    public static List<String> getQualifiedNames() {
        ArrayList<String> qualifiedNames = new ArrayList<>();
        for (String cat : getCategories()) {
            for (String texture : getTextureMapCategory(cat).keySet()) {
                qualifiedNames.add((cat + '.' + texture).toUpperCase());
            }
        }
        return Collections.unmodifiableList(qualifiedNames);
    }

    public static List<String> getTextures(String cat) {
        return Collections.unmodifiableList(new ArrayList<>(TextureMap.get(cat.toUpperCase()).keySet()));
    }

    /**
     * @see TextureHelper#toTextureJson(URL)
     */
    public static @NotNull JsonObject toTextureJson(URL url) {
        return toTextureJson(url.toExternalForm());
    }

    /**
     * Constructs a JsonObject that represents a Mojang Textures JSON object.
     * Note that it DOES NOT make sure the URL is safe or valid, as this is checked later.
     *
     * <p>A texture string is a Base64 encoded version of a JSON Object
     * <pre>
     * {
     *     "textures": {
     *        "SKIN": {
     *            "url": <b>&lt;TEXTURE URL&gt;</b>
     *        }
     *    }
     * }
     * </pre>
     *
     * @param url The URL of the texture file
     * @return A {@link JsonObject} representing a JSON object with structure similar to the following example:
     * <pre>
     * {
     *     "textures": {
     *        "SKIN": {
     *            "url": "http://textures.minecraft.net/texture/83cee5ca6afcdb171285aa00e8049c297b2dbeba0efb8ff970a5677a1b644032"
     *        }
     *    }
     * }
     * </pre>
     */
    public static @NotNull JsonObject toTextureJson(String url) {
        JsonObject SKIN = new JsonObject();
        SKIN.addProperty("url", url);

        JsonObject textures = new JsonObject();
        textures.add("SKIN", SKIN);

        JsonObject root = new JsonObject();
        root.add("textures", textures);

        return root;
    }

    /**
     * Creates a Base64-Encoded String containing the URL where the texture can be located.
     * @param url The URL of the texture
     * @return A Base-64 encoded version of the JSON provided by {@link TextureHelper#toTextureJson(String)}
     * 
     * @see TextureHelper#encodeJson(JsonObject)
     */
    public static String encodeTextureString(URL url) {
        return encodeJson(toTextureJson(url));
    }

    public static String encodeJson(JsonObject texture) {
        // todo Preconditions.checkArgument(validateTextureJson(texture));
        return StringUtil.encodeBase64(GSON.toJson(texture));
    }

    public static JsonObject decodeTextureStringToJson(String texture){
        return JsonParser.parseString(texture).getAsJsonObject();
    }


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
            Response resp = httpClient.newCall(new Request.Builder().url(textureURL).build()).execute();
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
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            StickyAPI.getLogger().log(Level.INFO, sw.toString());
            return false;
        }
    }

    public static String toQualifiedName(String filter, String head) {
        return (filter + '.' + head).toUpperCase();
    }
}
