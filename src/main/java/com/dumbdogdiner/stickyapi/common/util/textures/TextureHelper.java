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
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Contains various helper methods related to Textures, Texture Strings, and texture validation, as well as providing
 * access to the bundled texture resources
 */
//TODO write javadoc
@UtilityClass
public class TextureHelper {
    // Package-local visibility
    static final OkHttpClient httpClient = new OkHttpClient();

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




    public static String toQualifiedName(String filter, String head) {
        return (filter + '.' + head).toUpperCase();
    }
}
