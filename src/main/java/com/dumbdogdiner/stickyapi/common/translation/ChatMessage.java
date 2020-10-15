package com.dumbdogdiner.stickyapi.common.translation;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * A Wrapper Class for md_5's TextComponent API.
 */
public class ChatMessage {

    private TextComponent msg;
    private String raw;

    /**
     * Create a new ChatMessage object
     * @param content The preformatted Text, this ChatMessage
     *                should contain.
     */
    public ChatMessage(String content) {
        this.raw = content;
        TextComponent tc = new TextComponent(content);
        this.msg = tc;
    }

    /**
     * Set the ChatMessage content (overwrites all
     * appended ChatComponent objects!)
     *
     * @param content The new content this ChatMessage
     *                object should contain
     * @return The edited ChatMessage object
     */
    public ChatMessage setContent(String content) {
        this.msg = new TextComponent(content);
        this.raw = content;
        return this;
    }

    /**
     * Returns a String version of this ChatMessage object
     * @return The raw, unformatted message content
     */
    public String getRawContent() {
        return this.raw;
    }

    /**
     * Returns the final, formatted TextComponent of
     * this ChatMessage wrapper. TextComponents should
     * be sent via {@code Player#spigot().sendMessage(Component)}
     *
     * @return A Spigot-friendly TextComponent object
     */
    public TextComponent getComponent() {
        return msg;
    }

    /**
     * Optional method to format URLs within the message.
     * Converts all urls to a highlighted JSON clickable component.
     *
     * @param apply Whether URLs should be formatted.
     * @return The edited ChatMessage object
     */
    public ChatMessage applyURLs(boolean apply) {
        if(apply) {
            String original = this.msg.getText();
            msg = Translation.convertURLs(original);
        }

        return this;
    }

    /**
     * Append another ChatMessage object to this object.
     * @param chatMessage The ChatMessage that should be added.
     * @return The edited ChatMessage object
     */
    public ChatMessage appendMessage(ChatMessage chatMessage) {
        if(chatMessage != null) {
            this.msg.addExtra(chatMessage.getComponent());
            this.raw = raw + chatMessage.getRawContent();
        }


        return this;
    }

    /**
     * Optional method to add a Tooltip to this ChatMessage object.
     * @param text The String array that should be used for the tooltip content,
     *             each String represents a new line.
     * @return The edited ChatMessage object
     */
    public ChatMessage setTooltip(String... text) {
        StringBuilder tooltip = new StringBuilder();
        int i = 0;
        for(String s : text) {
            tooltip.append(s);
            if(i < text.length - 1) {
                tooltip.append(System.lineSeparator());
                i++;
            }
        }

        this.msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Translation.translateColors("&", tooltip.toString()))));
        return this;
    }

    /**
     * Optional method to add a URL to this ChatMessage object.
     * THIS DOES NOT format any URLs, use {@code ChatMessage#applyURLs(boolean)}
     * if you wish to format URLs within the message itself.
     *
     * @param url The URL this message should suggest when clicked.
     * @return The edited ChatMessage object
     */
    public ChatMessage setLink(String url) {
        this.msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    /**
     * Optional method to add a command to this message when clicked.
     *
     * @param command The command that should run when message is clicked.
     * @return The edited ChatMessage object
     */
    public ChatMessage setCommand(String command) {
        this.msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

}
