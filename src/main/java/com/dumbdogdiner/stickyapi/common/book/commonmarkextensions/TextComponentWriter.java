/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.Stack;

public class TextComponentWriter {
    private final Stack<TextComponent> levels = new Stack<>();
    private TextComponent current;
    private boolean renderNewLine = false;
    private boolean renderIndent = true;
    private int indent = 0;

    public TextComponentWriter(TextComponent component) {
        this.current = component;
    }

    public void enterLevel() {
        levels.push(current);
        current = new TextComponent();
    }

    public void exitLevel() {
        TextComponent top = levels.pop();
        top.addExtra(current);
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
            current.setText(current.getText() + "\n");
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

    public void setBold() {
        current.setBold(true);
    }

    public void setItalic() {
        current.setItalic(true);
    }

    public void setUnderlined() {
        current.setUnderlined(true);
    }

    public void setColor(ChatColor color) {
        current.setColor(color);
    }

    public void setHyperlink(String url) {
        if (url == null) {
            current.setClickEvent(null);
        } else {
            current.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        }
    }

    public void setMouseoverText(String text) {
        if (text == null) {
            current.setHoverEvent(null);
        } else {
            current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text)));
        }
    }
}
