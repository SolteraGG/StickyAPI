/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Utilities for text and books.
 */
@UtilityClass
public class BookUtil {
    /**
     * Offset for bold (in each direction)
     */
    public static final float BOLD_OFFSET = 0.5f;
    public static final float SHADOW_OFFSET = 0.5f;

    public static final int PIXELS_PER_LINE = 113;
    public static final int HALF_PIXELS_PER_LINE = PIXELS_PER_LINE * 2;

    public static final int LINES_PER_PAGE = 14;
    public static final int PAGES_PER_BOOK = 50;

    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<=[ \n])|(?=[ \n])");

    private static final HashMap<Character, Integer> widths = new HashMap<>();

    private static class WidthEntry {
        String uid;
        Integer id;
        String val;
        int width;
    }

    static {
        Gson gson = new Gson();
        try (InputStream input = ClassLoader.getSystemResource("mojangles_width_data.json").openStream()) {
            WidthEntry[] entries = gson.fromJson(new InputStreamReader(input), WidthEntry[].class);
            for (WidthEntry entry : entries) {
                if (entry.id == null) entry.id = 0;
                widths.put((char) (int) entry.id, entry.width);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param c The character to measure
     * @return The width of the character in half-pixels
     * @throws IllegalArgumentException if the character is out of range
     */
    public static int getCharacterWidth(char c) {
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
        int width = 0;
        int textLength = text.length();
        for (int i = 0; i < textLength; ++i) {
            width += BookUtil.getCharacterWidth(text.charAt(i));
        }
        if (isBold) width += 2 * textLength;
        return width;
    }

    public static int getStringWidth(@NonNull String text) {
        return getStringWidth(text, false);
    }
}
