/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.commonmarkextensions;

import com.dumbdogdiner.stickyapi.bukkit.book.chat.ComponentColor;
import org.bukkit.ChatColor;
import org.commonmark.node.*;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;

import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * A delimiter processor for custom name nbt.
 */
public class ColorFormatDelimiterProcessor implements DelimiterProcessor {
    private static final Pattern COLOR_PATTERN = Pattern.compile("^([a-z_]+|#[0-9A-Fa-f]{6})(\\s*(?:\\s[\\S\\s]+)?)$");

    @Override
    public char getOpeningCharacter() {
        return '$';
    }

    @Override
    public char getClosingCharacter() {
        return '$';
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int process(DelimiterRun opener, DelimiterRun closing) {
        if(opener.length() >= 1 && closing.length() == 1) {
            SourceSpans s = new SourceSpans();
            s.addAllFrom(opener.getOpeners(opener.length()));

            ComponentColor cc = ComponentColor.GOLD;
            Node n = opener.getOpener().getNext();
            //for (Node n : Nodes.between(opener.getOpener(), closing.getCloser())) {
                if (n instanceof Text) {
                    String literal = ((Text) n).getLiteral();
                    if (literal.charAt(0) == '#' && literal.length() == 7) {
                        cc = ComponentColor.fromText(literal.substring(1));
                        //break;
                    } else {
                        System.out.println(literal);
                        if(ChatColor.getByChar(literal.toLowerCase().charAt(0)) == null)
                            return 0;
                        System.out.println(literal);
                        cc = ComponentColor.fromColor(ChatColor.getByChar(literal.toLowerCase().charAt(0)).asBungee().getColor());
                    }
                    s.addAll(n.getSourceSpans());
                }

                MCColorNode mcn = new MCColorNode(cc);
                mcn.setSourceSpans(s.getSourceSpans());
                opener.getOpener().insertAfter(mcn);

            //}

/*
            SourceSpans sourceSpans = new SourceSpans();


            Node firstNext = opener.getNext();
            if (firstNext instanceof Text) {
                String literal = ((Text) firstNext).getLiteral();
                Matcher match = COLOR_PATTERN.matcher(literal);
                if (match.matches()) {
                    ((Text) firstNext).setLiteral(match.group(2).stripLeading());
                    ComponentColor color = ComponentColor.fromText(match.group(1));
                    if (color != null) {
                        MCColorNode colorNode = new MCColorNode(color);
                        Node next;
                        for (Node n = opener.getNext(); n != null && n != closer; n = next) {
                            next = n.getNext();
                            colorNode.appendChild(n);
                        }
                        opener.insertAfter(colorNode);
                    }
                }
            }*/
            return 1;
        } else
            return 0;
    }
}
