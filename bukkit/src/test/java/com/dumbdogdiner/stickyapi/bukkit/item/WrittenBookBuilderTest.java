/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.nbt.NbtCompoundTag;
import com.dumbdogdiner.stickyapi.nbt.NbtJsonTag;
import com.dumbdogdiner.stickyapi.nbt.NbtListTag;
import com.dumbdogdiner.stickyapi.nbt.NbtStringTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class WrittenBookBuilderTest {
    private static Reader bookReader;

    private static Gson G;


    @BeforeAll
    static void setUp(){
        G = new GsonBuilder().create();
    }


    @AfterEach
    void tearDown() throws IOException {
        bookReader.close();
        System.gc();
    }

    @AfterAll
    static void tearDownEnd() {
        G = null;
        System.gc();
    }


    @Test
    public void testSimple() {
        testJSON("/book1.json");
    }

    @Test
    public void testProdRulebook() {
        testJSON("/rulebook.json");
    }

    @Test
    void testNbtStuff() {
        bookReader = new InputStreamReader(WrittenBookBuilderTest.class.getResourceAsStream("/rulebook.json"));
        JsonObject bookObject = JsonParser.parseReader(bookReader).getAsJsonObject();



        NbtCompoundTag test123 = new NbtCompoundTag();
        test123.put("title", new NbtStringTag(bookObject.get("title").getAsString()));
        test123.put("author", NbtStringTag.fromPrimitive(bookObject.get("author").getAsJsonPrimitive()));
        NbtListTag pages = new NbtListTag();
        for(JsonElement element : bookObject.get("pages").getAsJsonArray()){
            pages.add(new NbtJsonTag(element));
        }
        test123.put("pages", pages);
        System.out.println("give @p minecraft:written_book" + test123.toNbtString());
    }

    public void testJSON(String filename) {
        bookReader = new InputStreamReader(WrittenBookBuilderTest.class.getResourceAsStream(filename));
        JsonObject bookObject = JsonParser.parseReader(bookReader).getAsJsonObject();
        WrittenBookBuilder bb = WrittenBookBuilder.fromJson(bookObject);
        System.out.println("Testing BookBuilder, please make sure the following NBT string is valid (It does not yet contain the title or author):");
        bb.addLoreLines(new NbtStringTag("The rules owo"));
        System.out.println(bb.generateNBT());

        System.out.println("Please paste the following line into a command block, and activate it");
        System.out.println("give @p minecraft:written_book" + bb.generateNBT());
    }
}
