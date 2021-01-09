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
     * Gets the {@link Set} of categories of textures in the Heads file
     * @return A {@link Set} of all the categories of texture
     */
    public static Set<String> getCategories() {
        return TextureMap.keySet();
    }

    /**
     * Get a {@link Set} of textures
     * @param cat The {@link #getCategories() category}of textures to get the {@link Map}
     * @see TextureHelper#getCategories()
     * @return A {@link Map} of texture names to texture strings for the given category
     */
    public static Map<String, String> getTextureMapCategory(String cat) {
        return TextureMap.get(cat.toUpperCase());
    }


    /**
     * Get a list of all the textures for a given Category
     * @param cat a Category of textures
     * @return a {@link List} of the textures in a given Category
     * @see #getCategories()
     */
    public static List<String> getTextures(String cat) {
        return Collections.unmodifiableList(new ArrayList<>(getTextureMapCategory(cat).keySet()));
    }

    /**
     * Gets a texture string given a category and texture name
     * @param cat The texture category
     * @param name The texture name
     * @return The texture string matching
     * @throws NoSuchElementException if the specified texture is not found
     */
    public static String getTexture(String cat, String name) throws NoSuchElementException {
        return TextureMap.get(cat.toUpperCase()).get(name.toUpperCase());
    }

    /**
     * Gets a texture string given a qualified name
     *
     * @param qualifiedName A given texture as a qualified name
     * @see #toQualifiedName(String, String)
     * @return the texture string for the given texture
     * @throws NoSuchElementException if the specified texture is not found
     */
    public static String getTexture(String qualifiedName) throws NoSuchElementException {
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

    /**
     * Returns a {@link List} of Qualified Names, of the format of "{CATEGORY}.{TEXTURE}"
     * @return a {@link List} of Qualified Names of all textures
     */
    public static List<String> getQualifiedNames() {
        ArrayList<String> qualifiedNames = new ArrayList<>();
        for (String cat : getCategories()) {
            for (String texture : getTextureMapCategory(cat).keySet()) {
                qualifiedNames.add(toQualifiedName(cat, texture));
            }
        }
        return Collections.unmodifiableList(qualifiedNames);
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
    public static String encodeTextureString(URL url) throws InvalidTextureException{
        TextureValidator.validateTextureUrl(url.toExternalForm());
        return encodeJson(toTextureJson(url));
    }

    /**
     * Encodes JSON wrapping a texture in Base64
     * @param texture JSON that wraps the texture URL
     * @return a base-64 encoded JSON that wraps a texture URL
     * @throws InvalidTextureException if the JSON is invalid
     */
    public static String encodeJson(JsonObject texture) throws InvalidTextureException{

        TextureValidator.validateTextureJson(texture);
        return StringUtil.encodeBase64(GSON.toJson(texture));
    }

    /**
     * Converts a texture string to a {@link JsonObject}
     * @param texture The texture string
     * @return a decoded {@link JsonObject}
     */
    public static JsonObject decodeTextureStringToJson(String texture){
        return JsonParser.parseString(texture).getAsJsonObject();
    }

    /**
     * Converts a category and head to a qualified name (in the form of <code>{category}.{name}</code>
     * @param category The category of head
     * @param name The specified name
     * @return a qualified name of the category and head
     */
    public static String toQualifiedName(String category, String name) {
        return String.join(".", category, name).toUpperCase();
    }
}
