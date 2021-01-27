/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.dumbdogdiner.stickyapi.common.book.chat.JsonComponent;
import com.google.gson.Gson;
import lombok.NonNull;
import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.node.ThematicBreak;

/**
 * Utilities for text and books.
 */
public class BookUtil {
    public static final int HALF_PIXELS_PER_LINE = 226;
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
        Integer width = widths.get(c);
        if (width != null) {
            return width;
        }
        throw new IllegalArgumentException("Unsupported character: " + c + " code: " +  String.format("%04x", (int) c));
    }

    /**
     * Split a JsonComponent into pages for use in a book.
     *
     * @param origComponent The component to split into pages
     * @return A list of JsonComponents, one for each page
     */
    public static List<JsonComponent> splitBookPages(@NonNull JsonComponent origComponent) {
        List<JsonComponent> lines = wrapLines(origComponent, HALF_PIXELS_PER_LINE);
        List<JsonComponent> pages = new ArrayList<>();
        JsonComponent currentPage = new JsonComponent();
        int i = 0;
        boolean trimNewLines = true;
        for (JsonComponent line : lines) {
            if (i++ % LINES_PER_PAGE == 0) {
                trimNewLines = true;
                pages.add(currentPage);
                currentPage = new JsonComponent();
            }
            if (trimNewLines) {
                if (line.getText().equals("\n")) {
                    line.setText("");
                } else {
                    trimNewLines = false;
                }
            }
            currentPage.getChildren().add(line);
        }
        pages.add(currentPage);
        return pages.subList(1, pages.size());
    }

    /**
     * TODO move this into the commonmark parser thing instead, because it will make life like 1000 times better!
     * Wrap a JsonComponent into lines.
     *
     * @param origComponent The JsonComponent to wrap.
     * @param halfPixels    The width of the available space in half-pixels.
     * @return A list of JsonComponents, one for each line
     */
    public static List<JsonComponent> wrapLines(@NonNull JsonComponent origComponent, int halfPixels) {
        List<JsonComponent> components = origComponent.flatten();
        if (components.isEmpty()) return new ArrayList<>();
        List<JsonComponent> lineComponents = new ArrayList<>();
        JsonComponent currentLine = new JsonComponent();
        int xPosition = 0;
        //TODO make this make better json plz its real crap
        for (JsonComponent component : components) {
            String[] words = SPLIT_PATTERN.split(component.getText());
            for (String word : words) {
                if (word.isEmpty()) continue;
                if (word.charAt(0) == '\n') {
                    int l = word.length();
                    for (int i = 0; i < l; ++i) {
                        lineComponents.add(currentLine);
                        currentLine = new JsonComponent("\n");
                    }
                    xPosition = 0;
                } else {
                    JsonComponent wordComponent = component.duplicate();
                    wordComponent.setText(word);
                    int width = getStringWidth(word, component.getBold() == Boolean.TRUE);
                    if (xPosition + width >= halfPixels) {
                        lineComponents.add(currentLine);
                        currentLine = new JsonComponent();
                        xPosition = width;
                    } else {
                        xPosition += width;
                    }
                    currentLine.getChildren().add(wordComponent);
                }
            }
        }
        if (!currentLine.getChildren().isEmpty()) {
            lineComponents.add(currentLine);
        }
        return lineComponents;
    }

    /**
     * Split a document into multiple documents, delimited by thematic breaks. Destroys the original document.
     *
     * @param document The original document
     * @return A list of each document.
     */
    public static List<Document> splitDocumentByBreaks(@NonNull Document document) {
        List<Document> docs = new ArrayList<>();
        Document currentDocument = new Document();
        Node next;
        for (Node n = document.getFirstChild(); n != null; n = next) {
            next = n.getNext();
            if (n instanceof ThematicBreak) {
                docs.add(currentDocument);
                currentDocument = new Document();
            } else {
                currentDocument.appendChild(n);
            }
        }
        docs.add(currentDocument);
        return docs;
    }

    /**
     * Measure the width of text in a BaseComponent in half-pixels.
     *
     * @param component The component to measure
     * @return The width of the text, in half-pixels
     */
    public static int getComponentWidth(@NonNull JsonComponent component) {
        String text = component.getText();
        int width = getStringWidth(text, component.getBold() == Boolean.TRUE);
        for (JsonComponent child : component.getChildren()) {
            width += getComponentWidth(child);
        }
        return width;
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
