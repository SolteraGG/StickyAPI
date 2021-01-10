/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.chat;

import com.dumbdogdiner.stickyapi.common.translation.Translation;
import com.dumbdogdiner.stickyapi.common.util.url.URLUtil;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * A Wrapper Class for md_5's TextComponent API.
 */
public class ChatMessage {

    /**
     * The local TextComponent this ChatMessage instance wraps. -- GETTER -- Returns
     * the final, formatted TextComponent of this ChatMessage wrapper.
     * TextComponents should be sent via
     * {@code Player#spigot().sendMessage(Component)}
     *
     * @return {@link net.md_5.bungee.api.chat.TextComponent}
     */
    @Getter
    private TextComponent component;

    /**
     * The raw content this ChatMessage instance wraps. -- GETTER -- Returns a
     * String version of this ChatMessage object
     * 
     * @return {@link java.lang.String}
     */
    @Getter
    private String rawContent;

    /**
     * Create a new ChatMessage object
     * 
     * @param content The preformatted Text, this ChatMessage should contain.
     */
    public ChatMessage(@NotNull String content) {
        this.rawContent = content;
        this.component = new TextComponent(content);
    }

    /**
     * Set the ChatMessage content - overwrites all appended ChatComponent objects!
     * Returns the edited ChatMessage object.
     *
     * @param content The new content this ChatMessage object should contain
     * @return {@link ChatMessage}
     */
    public ChatMessage setContent(@NotNull String content) {
        this.component = new TextComponent(content);
        this.rawContent = content;
        return this;
    }

    /**
     * Optional method to format URLs within the message. Converts all urls to a
     * highlighted JSON clickable component. Returns the edited ChatMessage object.
     *
     * @param apply Whether URLs should be formatted.
     * @return {@link ChatMessage}
     */
    public ChatMessage applyURLs(@NotNull boolean apply) {
        if (apply) {
            String original = this.component.getText();
            component = URLUtil.convertURLs(original);
        }

        return this;
    }

    /**
     * Append another ChatMessage object to this object. Returns the edited
     * ChatMessage object.
     *
     * @param chatMessage The ChatMessage that should be added.
     * @return {@link ChatMessage}
     */
    public ChatMessage appendMessage(@NotNull ChatMessage chatMessage) {
        if (chatMessage != null) {
            this.component.addExtra(chatMessage.getComponent());
            this.rawContent = rawContent + chatMessage.getRawContent();
        }
        return this;
    }

    /**
     * Optional method to add a Tooltip to this ChatMessage object. Returns the
     * edited ChatMessage object.
     * 
     * @param text The String array that should be used for the tooltip content,
     *             each String represents a new line.
     *
     * @return {@link ChatMessage}
     */
    public ChatMessage setHoverMessage(@NotNull String... text) {
        StringBuilder tooltip = new StringBuilder();
        int i = 0;
        for (String s : text) {
            tooltip.append(s);
            if (i < text.length - 1) {
                tooltip.append(System.lineSeparator());
                i++;
            }
        }

        this.component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(Translation.translateColors("&", tooltip.toString()))));
        return this;
    }

    /**
     * Optional method to add a URL to this ChatMessage object. THIS DOES NOT format
     * any URLs, use {@code ChatMessage#applyURLs(boolean)} if you wish to format
     * URLs within the message itself. Returns the edited ChatMessage object.
     *
     * @param url The URL this message should suggest when clicked.
     * @return {@link ChatMessage}
     */
    public ChatMessage setLink(@NotNull String url) {
        this.component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    /**
     * Optional method to add a command to this message when clicked. Returns the
     * edited ChatMessage object.
     *
     * @param command The command that should run when message is clicked.
     * @return {@link ChatMessage}
     */
    public ChatMessage setCommand(@NotNull String command) {
        this.component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

}
