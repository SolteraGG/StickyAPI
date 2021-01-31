/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.commonmarkextensions;

import org.commonmark.parser.Parser;



public class ParserFormatExtension implements Parser.ParserExtension {
    private ParserFormatExtension(){}

    public static ParserFormatExtension create() {
        return new ParserFormatExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new ColorFormatDelimiterProcessor());
        //parserBuilder.customDelimiterProcessor(new StrikethroughDelimiterProcessor());
        /*parserBuilder.postProcessor(null);
        parserBuilder.postProcessor(null);*/
        // TODO add a parser here that does the line splitting
    }
}
