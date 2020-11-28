package com.dumbdogdiner.stickyapi.common.util.commonmarkextensions;

import org.commonmark.Extension;
import org.commonmark.parser.Parser;

public class MCColorFormatExtension implements Parser.ParserExtension {
    private MCColorFormatExtension(){}

    public static Extension create(){
        return new MCColorFormatExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new MCColorFormatBlockParser.Factory());
    }
}
