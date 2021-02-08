/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.textures;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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
    private static final Gson GSON = new Gson();
    private static final Yaml YAML = new Yaml();
    private static Map<String, Map<String, String>> TextureMap = generateTextureMap();



    private static Map<String, Map<String, String>> generateTextureMap(){
        try {
            try (InputStream test = StickyAPI.getResourceAsStream("/generated/textures.json")) {
                //return YAML.load(test);
                Type mapType = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
                return GSON.fromJson(new InputStreamReader(test),mapType);
                //System.out.println(a.getClass());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("An unknown error occurred while accessing the builtin resource 'textures.yml'.", e);
            } catch (ClassCastException e) {
                throw new RuntimeException("The integrated textures.yml resource was invalid. Please check the format at compile-time. If you are a server owner, contact the developers of StickyAPI", e);
            }

        } catch (RuntimeException e) {
            Bukkit.getLogger().severe(e.getMessage());
            throw e;
        }
    }


    /**
     * Gets the {@link Set} of categories of textures in the Heads file
     *
     * @return A {@link Set} of all the categories of texture
     */
    public static @NotNull Set<String> getCategories() {
        return TextureMap.keySet();
    }

    /**
     * Get a {@link Set} of textures
     *
     * @param cat The {@link #getCategories() category}of textures to get the {@link Map}
     * @return A {@link Map} of texture names to texture strings for the given category
     * @see TextureHelper#getCategories()
     */
    public static Map<String, String> getTextureMapCategory(@NotNull String cat) {
        return TextureMap.get(cat.toUpperCase());
    }


    /**
     * Get a list of all the textures for a given Category
     *
     * @param cat a Category of textures
     * @return a {@link List} of the textures in a given Category
     * @see #getCategories()
     */
    public static @NotNull List<String> getTextures(@NotNull String cat) {
        return Collections.unmodifiableList(new ArrayList<>(getTextureMapCategory(cat).keySet()));
    }

    /**
     * Gets a texture string given a category and texture name
     *
     * @param cat  The texture category
     * @param name The texture name
     * @return The texture string matching
     * @throws NoSuchElementException if the specified texture is not found
     */
    public static String getTexture(@NotNull String cat, @NotNull String name) throws NoSuchElementException {
        return TextureMap.get(cat.toUpperCase()).get(name.toUpperCase());
    }

    /**
     * Gets a texture string given a qualified name
     *
     * @param qualifiedName A given texture as a qualified name
     * @return the texture string for the given texture
     * @throws NoSuchElementException if the specified texture is not found
     * @see #toQualifiedName(String, String)
     */
    public static @Nullable String getTexture(@NotNull String qualifiedName) throws NoSuchElementException {
        String[] splits = qualifiedName.split("\\.");
        if (splits.length != 2 && splits.length != 1)
            throw new RuntimeException("Invalid qualified name: " + qualifiedName);
        if (splits[0].equals("*")) {
            @Nullable String texture = null;
            for (@NotNull String cat : getCategories()) {
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

    /**
     * Returns a {@link List} of Qualified Names, of the format of "{CATEGORY}.{TEXTURE}"
     *
     * @return a {@link List} of Qualified Names of all textures
     */
    public static @NotNull List<String> getQualifiedNames() {
        @NotNull ArrayList<String> qualifiedNames = new ArrayList<>();
        for (@NotNull String cat : getCategories()) {
            for (String texture : getTextureMapCategory(cat).keySet()) {
                qualifiedNames.add(toQualifiedName(cat, texture));
            }
        }
        return Collections.unmodifiableList(qualifiedNames);
    }

    /**
     * @see TextureHelper#toTextureJson(URL)
     */
    public static @NotNull JsonObject toTextureJson(@NotNull URL url) {
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
        @NotNull JsonObject SKIN = new JsonObject();
        SKIN.addProperty("url", url);

        @NotNull JsonObject textures = new JsonObject();
        textures.add("SKIN", SKIN);

        @NotNull JsonObject root = new JsonObject();
        root.add("textures", textures);

        return root;
    }

    /**
     * Creates a Base64-Encoded String containing the URL where the texture can be located.
     *
     * @param url The URL of the texture
     * @return A Base-64 encoded version of the JSON provided by {@link TextureHelper#toTextureJson(String)}
     * @see TextureHelper#encodeJson(JsonObject)
     */
    public static @NotNull String encodeTextureString(@NotNull URL url) throws InvalidTextureException {
        TextureValidator.validateTextureUrl(url.toExternalForm());
        return encodeJson(toTextureJson(url));
    }

    /**
     * Encodes JSON wrapping a texture in Base64
     *
     * @param texture JSON that wraps the texture URL
     * @return a base-64 encoded JSON that wraps a texture URL
     * @throws InvalidTextureException if the JSON is invalid
     */
    public static @NotNull String encodeJson(@NotNull JsonObject texture) throws InvalidTextureException {

        TextureValidator.validateTextureJson(texture);
        return StringUtil.encodeBase64(GSON.toJson(texture));
    }

    /**
     * Converts a texture string to a {@link JsonObject}
     *
     * @param texture The texture string
     * @return a decoded {@link JsonObject}
     */
    public static JsonObject decodeTextureStringToJson(@NotNull String texture) {
        return JsonParser.parseString(texture).getAsJsonObject();
    }

    /**
     * Converts a category and head to a qualified name (in the form of <code>{category}.{name}</code>
     *
     * @param category The category of head
     * @param name     The specified name
     * @return a qualified name of the category and head
     */
    public static @NotNull String toQualifiedName(String category, String name) {
        return String.join(".", category, name).toUpperCase();
    }
}
