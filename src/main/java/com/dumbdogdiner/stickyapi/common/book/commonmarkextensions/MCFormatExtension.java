/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.book.commonmarkextensions;

import org.commonmark.Extension;
import org.commonmark.parser.Parser;

public class MCFormatExtension implements Parser.ParserExtension {
    private MCFormatExtension(){}

    public static Extension create(){
        return new MCFormatExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new MCColorFormatDelimiterProcessor());
        // TODO add a parser here that does the line splitting
    }
}
