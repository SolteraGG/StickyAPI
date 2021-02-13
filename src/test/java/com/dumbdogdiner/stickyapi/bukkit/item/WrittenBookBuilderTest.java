/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import com.dumbdogdiner.stickyapi.common.nbt.NbtCompoundTag;
import com.dumbdogdiner.stickyapi.common.nbt.NbtJsonTag;
import com.dumbdogdiner.stickyapi.common.nbt.NbtListTag;
import com.dumbdogdiner.stickyapi.common.nbt.NbtStringTag;
import com.dumbdogdiner.stickyapi.common.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;

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
        System.out.println("give @p minecraft:written_book" + test123.toSNbt());
    }

    public void testJSON(String filename) {
        bookReader = new InputStreamReader(WrittenBookBuilderTest.class.getResourceAsStream(filename));
        JsonObject bookObject = JsonParser.parseReader(bookReader).getAsJsonObject();
        WrittenBookBuilder bb = WrittenBookBuilder.fromJson(bookObject);
        System.out.println("Testing BookBuilder, please make sure the following NBT string is valid (It does not yet contain the title or author):");
        System.out.println(bb.generatePagesNBT());

        System.out.println("Please paste the following line into a command block, and activate it");
        System.out.println("give @p minecraft:written_book" + makeValidWrittenBookNBT(bb));
    }

    /**
     * Fix the command so it actually is valid, in a hacky way
     * @param bookBuilder Original bookbuilder
     * @return NBT that has the title forcibly put in
     */
    private String makeValidWrittenBookNBT(WrittenBookBuilder bookBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append(bookBuilder.generatePagesNBT());
        sb.insert(sb.length() - 1, StringUtil.formatChatCodes(MessageFormat.format(",title:\"{0}\",author:\"{1}\"", bookBuilder.getTitle(), bookBuilder.getAuthor())));
        return sb.toString();
    }
}
