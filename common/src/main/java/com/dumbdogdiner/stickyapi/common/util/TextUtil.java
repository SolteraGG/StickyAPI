/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import com.dumbdogdiner.stickyapi.StickyAPI;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.md_5.bungee.api.ChatColor.BOLD;
import static net.md_5.bungee.api.ChatColor.RESET;

/**
 * Utilities for text, such as in chat, books, and signs
 *
 * @since 3.0 (rewrite)
 */
@UtilityClass
public class TextUtil {
    /**
     * Offset for bold (in each direction) in half-pixels
     */
    public static final int BOLD_OFFSET = 1;

    /**
     * Number of pixels per line of a book
     */
    public static final int PIXELS_PER_BOOK_LINE = 113;
    /**
     * Number of half-pixels per book line
     */
    public static final int HALF_PIXELS_PER_BOOK_LINE = PIXELS_PER_BOOK_LINE * 2;

    /**
     * Number of lines in a page of a book
     */
    public static final int LINES_PER_PAGE = 14;
    /**
     * Number of pages in a book
     */
    public static final int PAGES_PER_BOOK = 50;

    /**
     * Number of pixels per line of a sign
     */
    // TODO verify via minecraft source code
    public static final int PIXELS_PER_SIGN_LINE = 96;
    /**
     * Number of half-pixels per line of a sign
     */
    public static final int HALF_PIXELS_PER_SIGN_LINE = PIXELS_PER_SIGN_LINE * 2;
    /**
     * Number of lines per sign
     */
    public static final int LINES_PER_SIGN = 4;

    /**
     * Number of pixels per line of chat
     */
    public static final int PIXELS_PER_CHAT_LINE = 250;
    /**
     * Number of half-pixels per line of chat
     */
    public static final int HALF_PIXELS_PER_CHAT_LINE = PIXELS_PER_CHAT_LINE * 2;

    private static final HashMap<Character, Integer> widths = new HashMap<>();

    private static final int SPACE_WIDTH;

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
            // fallback to a minimal configuration, in case anything fails
            for (char c = 32; c <= 126; ++c) {
                widths.put(c, 12);
            }
            // Extra fallback data to load in in case anything fails
            // Minecraft font data is formatted in a very particular way
            // so we are forced to use unfriendly code to load in extra fallback data.
            "\0 ".chars().forEach(c -> widths.put((char) c, 2));
            "!',.:;i|".chars().forEach(c -> widths.put((char) c, 4));
            "`l".chars().forEach(c -> widths.put((char) c, 6));
            // Some characters are represented by their Unicode code point to ensure they
            // load correctly
            "\"()*I[]t{}\u2022".chars().forEach(c -> widths.put((char) c, 8));
            "<>fk\u00b7".chars().forEach(c -> widths.put((char) c, 10));
            "@~".chars().forEach(c -> widths.put((char) c, 14));
        }

        SPACE_WIDTH = getCharacterWidth(' ');
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
    public static int getCharacterWidth(char c) throws IllegalArgumentException {
        Preconditions.checkArgument(widths.containsKey(c), "Unsupported character: " + c + " code: " + String.format("%04x", (int) c));

        return widths.get(c);
    }

    /**
     * @param c The character to measure
     * @param bold If the character is bold
     * @return The width of the character in half-pixels
     * @throws IllegalArgumentException if the character is out of range
     */
    public static int getCharacterWidth(char c, boolean bold) throws IllegalArgumentException {
        Preconditions.checkArgument(widths.containsKey(c), "Unsupported character: " + c + " code: " + String.format("%04x", (int) c));

        return widths.get(c) + (bold ? 2 * BOLD_OFFSET : 0);
    }

    /**
     * Measure the width of a string, assuming it is in the default Minecraft font.
     *
     * @param text The string to measure
     * @return The width of the string, in half-pixels
     */
    public static int getStringWidth(@NonNull String text) {
        int width = 0;
        BaseComponent[] components = TextComponent.fromLegacyText(text);
        for (BaseComponent component : components) {
            for (char c : component.toPlainText().toCharArray()) {
                width += getCharacterWidth(c);
            }
            // TODO double check this calculation, based off of koda's code and something i read but cant find anymore
            if (component.isBold()) {
                width += (component.toPlainText().length() + 1) * 2;
            }
        }

        return width;
    }

    public static String[] splitLines(String... lines) {

        List<String> processedLines = new ArrayList<>();
        for (String line : lines) {
            StringBuilder processedLine = new StringBuilder();
            ChatFormatStack formatStack = new ChatFormatStack();
            boolean chatCode = false;
            int width = 0;
            for (char c : line.toCharArray()) {
                if (c == ChatColor.COLOR_CHAR) {
                    chatCode = true;
                    processedLine.append(c);
                } else if (chatCode) {
                    if(formatStack.push(ChatColor.getByChar(c)) == null) {
                        // Remove format code if it's not valid
                        processedLine.deleteCharAt(processedLine.length() - 1);
                    } else {
                        // Put in the code if it is valid
                        processedLine.append(c);
                    }
                    chatCode = false;
                } else {
                    int charWidth = getCharacterWidth(c, formatStack.isBold());
                    if (width + charWidth > HALF_PIXELS_PER_CHAT_LINE) {
                        // Start a new line!
                        processedLines.add(processedLine.toString());

                        processedLine = new StringBuilder();
                        width = 0;

                        processedLine.append(formatStack.toString());
                    }
                    // Append to the current (possibly new) line
                    processedLine.append(c);
                    width += charWidth;
                }
            }
            processedLines.add(processedLine.toString());
        }

        return processedLines.toArray(new String[0]);
    }

    public static String[] prettifyText(String... text) {
        text = splitLines(text);
        for (int i = 0; i < text.length; i++) {
            int width = getStringWidth(text[i]);
            int pad = Math.round((HALF_PIXELS_PER_CHAT_LINE - width) / 2f / SPACE_WIDTH) / 2;
            text[i] = Strings.padStart("", pad, ' ') + text[i];
        }
        return text;
    }


    @SuppressWarnings("UnusedReturnValue")
    private static final class ChatFormatStack {
        private final ArrayDeque<ChatColor> stack;
        public ChatFormatStack() {
            stack = new ArrayDeque<>();
            //push(RESET);
        }

        public ChatColor push(ChatColor c) {
            if(c == null)
                return c;
            if (c == RESET)
                stack.clear();

            stack.addLast(c);

            return c;
        }

        public ChatColor pop(ChatColor c) {
            return stack.removeLast();
        }

        public void clear() {
            stack.clear();
        }

        /**
         * @return If the formats that are to be applied include Bold
         */
        public boolean isBold(){
            return stack.contains(BOLD);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            stack.forEach(sb::append);
            return sb.toString();
        }
    }
}
