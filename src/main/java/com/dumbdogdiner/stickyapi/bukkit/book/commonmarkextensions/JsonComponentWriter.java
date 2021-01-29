/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.commonmarkextensions;

import com.dumbdogdiner.stickyapi.bukkit.book.chat.ClickEvent;
import com.dumbdogdiner.stickyapi.bukkit.book.chat.ComponentColor;
import com.dumbdogdiner.stickyapi.bukkit.book.chat.HoverEvent;
import com.dumbdogdiner.stickyapi.bukkit.book.chat.JsonComponent;
import lombok.Getter;

import java.util.Stack;

/**
 * A class for easily writing to a tree of {@link JsonComponent}s.
 */
public class JsonComponentWriter {
    private final Stack<JsonComponent> levels = new Stack<>();
    private JsonComponent current;
    private boolean renderNewLine = false;
    private boolean renderIndent = true;
    @Getter
    private int indent = 0;

    public JsonComponentWriter(JsonComponent component) {
        this.current = component;
    }

    /**
     * Enter a deeper level in the tree.
     */
    public void enterLevel() {
        levels.push(current);
        current = new JsonComponent();
    }

    /**
     * Exit the current level.
     */
    public void exitLevel() {
        JsonComponent top = levels.pop();
        top.getChildren().add(current);
        current = top;
    }

    /**
     * Increase the indentation of the next line.
     */
    public void indent() {
        ++indent;
    }

    /**
     * Decrease the indentation of the next line.
     */
    public void dedent() {
        indent = Math.max(indent - 1, 0);
    }

    /**
     * Add some text to the JsonComponent tree.
     * @param text The text to add
     */
    public void addText(String text) {
        if (text.isEmpty()) return;
        enterLevel();
        if (renderNewLine) {
            renderNewLine = false;
            current.setText(current.getText() + "\n\n");
            renderIndent = true;
        }
        if (renderIndent) {
            renderIndent = false;
            current.setText(current.getText() + "  ".repeat(indent));
        }
        current.setText(current.getText() + text);
        exitLevel();
    }

    /**
     * Add a line break.
     */
    public void addLine() {
        renderNewLine = true;
    }

    /**
     * Set if the text in the current level of the tree is bold.
     */
    public void setBold(Boolean bold) {
        current.setBold(bold);
    }

    /**
     * Set if the text in the current level of the tree is italic.
     */
    public void setItalic(Boolean italic) {
        current.setItalic(italic);
    }

    /**
     * Set if the text in the current level of the tree is underlined.
     */
    public void setUnderlined(Boolean underlined) {
        current.setUnderlined(underlined);
    }

    /**
     * Set if the text in the current level of the tree is strikethrough.
     */
    public void setStrikethrough(Boolean strikethrough) {
        current.setStrikethrough(strikethrough);
    }

    /**
     * Set if the text in the current level of the tree is obfuscated.
     */
    public void setObfuscated(Boolean obfuscated) {
        current.setObfuscated(obfuscated);
    }

    /**
     * Set the color of the text in the current level of the tree.
     */
    public void setColor(ComponentColor color) {
        current.setColor(color);
    }

    /**
     * Set the {@link ClickEvent} in the current level of the tree.
     */
    public void setClickEvent(ClickEvent event) {
        current.setClickEvent(event);
    }

    /**
     * Set the {@link HoverEvent} in the current level of the tree.
     */
    public void setHoverEvent(HoverEvent event) {
        current.setHoverEvent(event);
    }
}
