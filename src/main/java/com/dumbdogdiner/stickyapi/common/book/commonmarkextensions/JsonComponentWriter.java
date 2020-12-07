/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import com.dumbdogdiner.stickyapi.common.book.chat.ClickEvent;
import com.dumbdogdiner.stickyapi.common.book.chat.ComponentColor;
import com.dumbdogdiner.stickyapi.common.book.chat.HoverEvent;
import com.dumbdogdiner.stickyapi.common.book.chat.JsonComponent;

import java.util.Stack;

public class JsonComponentWriter {
    private final Stack<JsonComponent> levels = new Stack<>();
    private JsonComponent current;
    private boolean renderNewLine = false;
    private boolean renderIndent = true;
    private int indent = 0;

    public JsonComponentWriter(JsonComponent component) {
        this.current = component;
    }

    public void enterLevel() {
        levels.push(current);
        current = new JsonComponent();
    }

    public void exitLevel() {
        JsonComponent top = levels.pop();
        top.getChildren().add(current);
        current = top;
    }

    public void indent() {
        ++indent;
    }

    public void dedent() {
        indent = Math.max(indent - 1, 0);
    }

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

    public void addLine() {
        renderNewLine = true;
    }

    public void setBold(Boolean bold) {
        current.setBold(bold);
    }

    public void setItalic(Boolean italic) {
        current.setItalic(italic);
    }

    public void setUnderlined(Boolean underlined) {
        current.setUnderlined(underlined);
    }

    public void setStrikethrough(Boolean strikethrough) {
        current.setStrikethrough(strikethrough);
    }

    public void setObfuscated(Boolean obfuscated) {
        current.setObfuscated(obfuscated);
    }

    public void setColor(ComponentColor color) {
        current.setColor(color);
    }

    public void setClickEvent(ClickEvent event) {
        current.setClickEvent(event);
    }

    public void setHoverEvent(HoverEvent event) {
        current.setHoverEvent(event);
    }
}
