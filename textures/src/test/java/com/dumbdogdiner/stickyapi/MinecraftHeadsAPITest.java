/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi;

import com.dumbdogdiner.stickyapi.common.util.MathUtil;
import com.dumbdogdiner.stickyapi.util.http.HttpException;
import com.dumbdogdiner.stickyapi.util.textures.InvalidTextureException;
import com.dumbdogdiner.stickyapi.util.textures.TextureValidator;
import com.google.common.base.CharMatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import com.dumbdogdiner.stickyapi.util.textures.headdatabase.MinecraftHeadsAPI;
import org.opentest4j.TestAbortedException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MinecraftHeadsAPITest {
    private static final int TEST_REPS = 300;
    private static List<HeadThing> heads = new ArrayList<>();

    @BeforeAll
    public static void setUp() throws IOException {
        List<HeadThing> allHeads = new ArrayList<>();
        CSVParser parser = new CSVParser(
                new InputStreamReader(MinecraftHeadsAPITest.class.getResourceAsStream("/Custom-Head-DB.csv")),
                CSVFormat.newFormat(';')
                        .withQuote('"')
                        .withQuoteMode(QuoteMode.MINIMAL)
                        .withSkipHeaderRecord()
                        .withHeader("Category", "ID", "Name", "Texture", "na1", "na2"));
        for(CSVRecord rec : parser){
            allHeads.add(new HeadThing(rec.get("Category"), rec.get("ID"), rec.get("Name"), rec.get("Texture")));
        }

        for (int i = 0; i < TEST_REPS; i++) {
            HeadThing thing;
            //noinspection StatementWithEmptyBody
            while (heads.contains(thing = MathUtil.randomElement(allHeads)));
            heads.add(thing);
        }
    }

    @AfterAll
    public static void tearDown(){
        heads = null;
        System.gc();
    }

    @RepeatedTest(TEST_REPS)
    void get(RepetitionInfo repetitionInfo) {
        HeadThing head = heads.get(repetitionInfo.getCurrentRepetition() - 1);
        int id = head.getId();
        System.out.println("Testing ID " + id + " - " + head.getName());
        try {
            String textureString = MinecraftHeadsAPI.getTextureString(id);
            String name = MinecraftHeadsAPI.getName(id);
            assumeTrue(CharMatcher.ascii().matchesAllOf(name), "Original name is not ASCII");
            assumeTrue(CharMatcher.ascii().matchesAllOf(head.getName()), "Web name is not ascii");
            assertEquals(head.getName(), name);
            assertTrue(MinecraftHeadsAPI.getTextureUrl(id).toExternalForm().endsWith(head.getTexture()));
            assumeTrue(TextureValidator.isValidTextureUrl("http://textures.minecraft.net/texture/" + head.getTexture()), "Original texture is not valid");
            assertTrue(TextureValidator.isValidTextureString(textureString));
        } catch (InvalidTextureException e) {
            Throwable th = e.getCause();
            if(th instanceof HttpException){
                // Ignore tests where a blank document is returned
                assumeTrue(th.getMessage().contains("Null Document"));
            }
            e.printStackTrace();
        } catch (TestAbortedException e){
            // Print whatever assumption was failed/where, and then pass it on
            e.printStackTrace();
            throw e;
        }

    }

    @AllArgsConstructor
    static class HeadThing {
        @Getter
        private final String category;
        private final String id;
        @Getter
        private final String name;
        @Getter
        private final String texture;

        public int getId(){
            return Integer.parseInt(id);
        }
    }
}