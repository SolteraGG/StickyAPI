/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import com.dumbdogdiner.stickyapi.common.book.chat.ComponentColor;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A delimiter processor for custom name nbt.
 */
public class MCColorFormatDelimiterProcessor implements DelimiterProcessor {
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
    public int getDelimiterUse(DelimiterRun opener, DelimiterRun closer) {
        if (opener.length() >= 1 && closer.length() >= 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void process(Text opener, Text closer, int delimiterUse) {
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
        }
    }
}
