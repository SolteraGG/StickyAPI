/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book.commonmarkextensions;

import org.commonmark.node.Block;
import org.commonmark.parser.block.AbstractBlockParser;
import org.commonmark.parser.block.BlockContinue;
import org.commonmark.parser.block.ParserState;

public class MCBlockParser extends AbstractBlockParser {
    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        return null;
    }
}
