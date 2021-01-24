/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.dumbdogdiner.stickyapi.common.book.chat.JsonComponent;
import lombok.NonNull;
import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.node.ThematicBreak;

import java.awt.Font;


/**
 * Utilities for text and books.
 */
public class BookUtil {
    public static final int PIXELS_PER_LINE = 113;
    public static final int LINES_PER_PAGE = 14;
    public static final int PAGES_PER_BOOK = 50;

    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<=[ \n])|(?=[ \n])");

    /**
     * Uses info from https://minecraft.gamepedia.com/Language#Font"
     *
     * @param c The character to measure
     * @return The width of the character in pixels
     * @throws IllegalArgumentException if the character is out of range
     */
    public static int getCharacterWidth(char c) {

        switch (c) {
            case '!':
            case ',':
            case '\'':
            case '.':
            case ':':
            case ';':
            case 'i':
            case '|':
                return 1;
            case '`':
            case 'l':
                return 2;
            case ' ':
            case '(':
            case ')':
            case '*':
            case 'I':
            case '[':
            case ']':
            case 't':
            case '{':
            case '}':
            case '\u2022':
                return 3;
            case '<':
            case '>':
            case 'f':
            case 'k':
            case '\u00b7':
                return 4;
            case '@':
            case '~':
                return 6;
        }
        if (c >= 32 && c <= 126) {
            return 5;
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
        List<JsonComponent> lines = wrapLines(origComponent, PIXELS_PER_LINE);
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
     * Wrap a JsonComponent into lines.
     *
     * @param origComponent The JsonComponent to wrap.
     * @param pixels        The width of the JsonComponent in pixels.
     * @return A list of JsonComponents, one for each line
     */
    public static List<JsonComponent> wrapLines(@NonNull JsonComponent origComponent, int pixels) {
        List<JsonComponent> components = origComponent.flatten();
        if (components.isEmpty()) return new ArrayList<>();
        List<JsonComponent> lineComponents = new ArrayList<>();
        JsonComponent currentLine = new JsonComponent();
        int xPosition = 0;
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
                    int width = getStringWidth(word) + 1;
                    if (component.getBold() == Boolean.TRUE) width += word.length() + 1;
                    if (xPosition + width >= pixels) {
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
     * Measure the width of text in a BaseComponent in pixels.
     *
     * @param component The component to measure
     * @return The width of the text, in pixels
     */
    public static int getComponentWidth(@NonNull JsonComponent component) {
        String text = component.getText();
        int width = getStringWidth(text);
        if (component.getBold() == Boolean.TRUE) width += text.length();
        for (JsonComponent child : component.getChildren()) {
            width += getComponentWidth(child);
        }
        return width;
    }

    /**
     * Measure the width of a string, assuming it is unformatted and in the default Minecraft font.
     *
     * @param text The string to measure
     * @return The width of the string, in pixels
     */
    public static int getStringWidth(@NonNull String text) {
        if (text.isEmpty()) return 0;
        int width = 0;
        int textLength = text.length();
        for (int i = 0; i < textLength; ++i) {
            width += BookUtil.getCharacterWidth(text.charAt(i));
        }
        width += textLength - 1;
        return width;
    }
}
