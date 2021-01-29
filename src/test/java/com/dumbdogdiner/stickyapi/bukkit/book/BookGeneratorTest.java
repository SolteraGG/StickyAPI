/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.book;

import com.dumbdogdiner.stickyapi.bukkit.item.generator.BookGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.awt.print.Book;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookGeneratorTest {
    @Test
    public void testJsonBuild(){
        InputStream rulebookStream = BookGeneratorTest.class.getResourceAsStream("/rulebook.json");
        JsonObject jso = (JsonObject) JsonParser.parseReader(new InputStreamReader(rulebookStream));
        System.out.println(jso.toString());
        BookGenerator bg = BookGenerator.fromJson(jso);
        System.out.println(bg.generateNbtString());
    }
}
