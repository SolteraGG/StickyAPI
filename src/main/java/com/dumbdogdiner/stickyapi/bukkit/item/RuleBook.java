/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.common.util.commonmarkextensions.MCColorFormatDelimiterProcessor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuleBook {
    //TODO MOVE TO CONST
    private static final String RULEBOOK_PATH = "plugin name i guess? /rulebook.md";
    public static void generateDefault() throws IOException {
        Parser cmparser = Parser.builder()
                .customDelimiterProcessor(new MCColorFormatDelimiterProcessor())
                .build();
        File file = new File(RULEBOOK_PATH);
        Node bookRoot  = cmparser.parseReader(new InputStreamReader(new FileInputStream(new File(RULEBOOK_PATH))));
        //TODO: Traverse all subnodes and build the relevant json, convert to nbt, create a bookmeta, create an itemstack, and return it

    }
}
