/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCColorFormatDelimiterProcessor implements DelimiterProcessor {
    private static final Pattern COLOR_PATTERN = Pattern.compile("^(black|dark_blue|dark_green|dark_aqua|dark_red|dark_purple|gold|gray|dark_gray|blue|green|aqua|red|light_purple|yellow|white|reset|#[0-9A-Fa-f]{6})(\\s*(?:\\s[\\S\\s]+)?)$");

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
            // only create the color node if a color was specified
            if (match.matches()) {
                ((Text) firstNext).setLiteral(match.group(2).stripLeading());
                MCColorNode color = new MCColorNode(match.group(1));
                Node next;
                for (Node n = opener.getNext(); n != null && n != closer; n = next) {
                    next = n.getNext();
                    color.appendChild(n);
                }
                opener.insertAfter(color);
            }
        }
    }
}
