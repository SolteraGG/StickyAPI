/*
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.textures;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.validator.routines.UrlValidator;
import org.w3c.dom.Text;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;


//TODO write javadoc
public class TextureHelper {
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a};
    private static final OkHttpClient httpClient = new OkHttpClient();

    private static Map<String, Map<String, String>> TextureMap;

    static {
        Yaml y = new Yaml();
        try (InputStream test = StickyAPI.getResourceAsStream("/textures.yml")) {
            Object a = y.load(test);
            //TODO do something neater/nicer plz
            TextureMap = (Map<String, Map<String, String>>) a;
           //System.out.println(a.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassCastException e){
            throw new RuntimeException("The integrated textures.yml resource was invalid. Please check the format at compile-time. If you are a server owner, contact the developers of StickyAPI", e);
        }
    }


    public static Set<String> getCategories(){
        return TextureMap.keySet();
    }

    public static Map<String, String> getTexturesCategory(String cat){
        return TextureMap.get(cat.toUpperCase());
    }

    public static Set<String> getTexturesByCategory(String cat){
        return TextureMap.get(cat.toUpperCase()).keySet();
    }

    public static String getTexture(String cat, String name){
        return TextureMap.get(cat.toUpperCase()).get(name.toUpperCase());
    }

    public static String getTexture(String qualifiedName){
        String [] splits = qualifiedName.split("\\.");
        if(splits.length != 2 && splits.length != 1)
            throw new RuntimeException("Invalid qualified name: " + qualifiedName);
        if(splits[1].equals("*")){
            String texture = null;
            for(String cat : getCategories()){
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

    public static List<String> getQualifiedNames(){
        ArrayList<String> qualifiedNames = new ArrayList<>();
        for(String cat : getCategories()){
            for(String texture : getTexturesByCategory(cat)){
                qualifiedNames.add((cat + '.' + texture).toUpperCase());
            }
        }
        return Collections.unmodifiableList(qualifiedNames);
    }

    public static List<String> getTextures(String cat){
        return Collections.unmodifiableList(new ArrayList<>(TextureMap.get(cat.toUpperCase()).keySet()));
    }

    //Exists to defeat instantiation

    private TextureHelper() {

    }



    /**
     * Attempts to (as exhaustively as possible) validate a given String of a Base-64 encoded texture
     *
     * @param texture The string that represents the texture
     * @return if the texture is valid or not
     */
    public static boolean validateTexture(String texture) {
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
}
