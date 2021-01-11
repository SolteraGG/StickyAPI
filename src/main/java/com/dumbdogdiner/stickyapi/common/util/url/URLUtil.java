/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util.url;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {

    private static final Pattern urlPattern = Pattern.compile(
            "(https:\\/\\/|http:\\/\\/)((?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?");

    /**
     * Find the first URL in a given Text.
     * <p>
     * Returns a URLPair object which stores the full URL as well as a shortened
     * version (e.g. www.github.com)
     * 
     * @param text The text that should be checked for URLs
     * @return {@link URLPair}
     */
    public static URLPair findURL(@NotNull String text) {
        Matcher matcher = urlPattern.matcher(text);

        if (matcher.find()) {
            return new URLPair(matcher.group(0), matcher.group(2));
        }
        return null;
    }

    /**
     * Converts URLs in a preformatted String to clickable JSON components.
     * <p>
     * Returns a TextComponent containing formatted and clickable URLs.
     * 
     * @param text The text that should be converted into a TextComponent with
     *             formatted URLs.
     * @return {@link TextComponent}
     */

    public static TextComponent convertURLs(@NotNull String text) {
        TextComponent finalComp = new TextComponent();
        TextComponent tmp = new TextComponent();

        String[] split = text.split(" ");
        int i = 0;
        for(String s : split) {
            URLPair url = findURL(s + " ");
            if((url) == null) {
                tmp.setText(tmp.getText() + s + " ");
                if(split.length == i + 1) finalComp.addExtra(tmp);
            } else {
                finalComp.addExtra(tmp);
                tmp = new TextComponent();

                TextComponent urlComponent = new TextComponent(url.getShortened() + " ");
                urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.getFullPath()));
                urlComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.COLOR_CHAR + "" + ChatColor.GRAY + "Click to open URL"), new Text("\n§8" + url.getFullPath())));
                urlComponent.setBold(true);
                finalComp.addExtra(urlComponent);
            }

            i++;
        }

        return finalComp;
    }

}
