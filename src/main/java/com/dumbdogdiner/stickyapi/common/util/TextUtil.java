/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ChatColor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

/**
 * Utilities for text, such as in chat, books, and signs
 *
 * @since TBA (rewritten)
 */
public class TextUtil {
    private TextUtil() {
    }

    /** Offset for bold (in each direction) */
    public static final float BOLD_OFFSET = 0.5f;
    /** Offset for shadows (in each direction) */
    public static final float SHADOW_OFFSET = 0.5f;

    /** Number of pixels per line of a book */
    public static final int PIXELS_PER_BOOK_LINE = 113;
    /** Number of half-pixels per book line */
    public static final int HALF_PIXELS_PER_BOOK_LINE = PIXELS_PER_BOOK_LINE * 2;

    /** Number of lines in a page of a book */
    public static final int LINES_PER_PAGE = 14;
    /** Number of pages in a book */
    public static final int PAGES_PER_BOOK = 50;

    /** Number of pixels per line of a sign */
    // TODO verify via minecraft source code
    public static final int PIXELS_PER_SIGN_LINE = 96;
    /** Number of lines per sign */
    public static final int LINES_PER_SIGN = 4;

    private static final HashMap<Character, Integer> widths = new HashMap<>();

    /**
     * Internal class to parse the width data json file
     */
    @Data
    private static class WidthEntry {
        private @NotNull String uid;
        private @Nullable Integer id;
        private @NotNull String val;
        private int width;
    }

    static {
        Gson gson = new Gson();
        try (InputStream input = ClassLoader.getSystemResource("generated/mojangles_width_data.json").openStream()) {
            WidthEntry[] entries = gson.fromJson(new InputStreamReader(input), WidthEntry[].class);
            for (WidthEntry entry : entries) {
                if (entry.getId() == null) entry.setId(0);
                widths.put((char) (entry.getId() != null ? entry.getId() : '\0'), entry.width);
            }
        } catch (Exception e) {
            StickyAPI.getLogger().severe(e.getMessage());
            // fallback to a minimal , in case anything fails
            for (char c = 32; c <= 126; ++c) {
                widths.put(c, 12);
            }
            "\0 ".chars().forEach(c -> widths.put((char) c, 2));
            "!',.:;i|".chars().forEach(c -> widths.put((char) c, 4));
            "`l".chars().forEach(c -> widths.put((char) c, 6));
            "\"()*I[]t{}\u2022".chars().forEach(c -> widths.put((char) c, 8));
            "<>fk\u00b7".chars().forEach(c -> widths.put((char) c, 10));
            "@~".chars().forEach(c -> widths.put((char) c, 14));
        }
    }

    /**
     * Check if the given character is supported in-game.
     * 
     * @param c The character to check
     * @return A boolean representing if the character is supported or not
     */
    public static boolean isCharacterSupported(char c) {
        return widths.containsKey(c);
    }


    /**
     * Returns a {@link Set} of {@link Character}of the supported characters
     */
    public static Set<Character> getSupportedCharacters() {
        return widths.keySet();
    }

    /**
     * @param c The character to measure
     * @return The width of the character in half-pixels
     * @throws IllegalArgumentException if the character is out of range
     */
    public static int getCharacterWidth(char c) throws IllegalArgumentException{
        Preconditions.checkArgument(widths.containsKey(c), "Unsupported character: " + c + " code: " +  String.format("%04x", (int) c));

        return widths.get(c);
    }

    /**
     * Measure the width of a string, assuming it is in the default Minecraft font.
     *
     * @param text The string to measure
     * @param isBold If the string is bold
     * @return The width of the string, in half-pixels
     */
    public static int getStringWidth(@NonNull String text, boolean isBold) {
        text = ChatColor.stripColor(text);
        int width = 0;

        for (char c : text.toCharArray()) {
            width += TextUtil.getCharacterWidth(c);
        }

        if (isBold)
            width += 2 *  text.length();

        return width;
    }

    /**
     Measure the width of a string, assuming it is in the default Minecraft font.
     *
     * @param text The string to measure
     * @return The width of the string, in half-pixels
     * @see #getStringWidth(String, boolean)
     */
    public static int getStringWidth(@NonNull String text) {
        return getStringWidth(text, false);
    }
}
