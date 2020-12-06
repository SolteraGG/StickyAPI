/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.List;

//TODO Implement NodeRenderer or Renderer
public class MarkdownJsonRenderer {
    private MarkdownJsonRenderer() {}

    public static BaseComponent render(Node node) {
        BaseComponent component = new MarkdownJsonRenderer().render0(node);
        BaseComponent current = component;
        // find last element and try to remove trailing newlines
        while (!current.getExtra().isEmpty()) {
            current = current.getExtra().get(current.getExtra().size() - 1);
        }
        if (current instanceof TextComponent) {
            ((TextComponent) current).setText(((TextComponent) current).getText().stripTrailing());
        }
        return component;
    }

    private BaseComponent render0(Node node) {
        TextComponent component = new TextComponent();
        addProperties(node, component);
        component.setExtra(renderChildren(node));
        addExtra(node, component);
        return optimize(component);
    }

    private void addProperties(Node node, TextComponent component) {
        if (node instanceof Text) {
            component.setText(((Text) node).getLiteral());
        } else if (node instanceof Heading) {
            component.setBold(true);
        } else if (node instanceof MCColorNode) {
            component.setColor(ChatColor.of(((MCColorNode) node).getColorName()));
        } else if (node instanceof Delimited) {
            switch (((Delimited) node).getOpeningDelimiter()) {
                case "*": case "_":
                    component.setItalic(true);
                    break;
                case "**":
                    component.setBold(true);
                    break;
                case "__":
                    component.setUnderlined(true);
                    break;
                    // no official formatters are mapped to strikethrough or obfuscated
            }
        } else if (node instanceof Link) {
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ((Link) node).getDestination()));
            String title = ((Link) node).getTitle();
            if (title != null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.hover.content.Text(title)));
            }
        }
    }

    private void addExtra(Node node, TextComponent component) {
        if (node instanceof SoftLineBreak) {
            component.addExtra(" ");
        } else if (node instanceof HardLineBreak) {
            component.addExtra("\n");
        } else if (node instanceof Block) {
            if (!(
                    node instanceof ListItem ||
                    node instanceof ListBlock
            )) {
                component.addExtra("\n");
            }
        }
    }

    private List<BaseComponent> renderChildren(Node node) {
        List<BaseComponent> extra = new ArrayList<>();
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            extra.add(render0(child));
        }
        return extra;
    }

    private BaseComponent optimize(TextComponent component) {
        if (component.getText().equals("") &&
                component.getExtra().size() == 1 &&
                (component.isBoldRaw() == Boolean.FALSE) &&
                (component.isItalicRaw() == Boolean.FALSE) &&
                (component.isObfuscatedRaw() == Boolean.FALSE) &&
                (component.isStrikethroughRaw() == Boolean.FALSE) &&
                (component.isUnderlinedRaw() == Boolean.FALSE)
        ) {
            return component.getExtra().get(0);
        } else {
            return component;
        }
    }
}
