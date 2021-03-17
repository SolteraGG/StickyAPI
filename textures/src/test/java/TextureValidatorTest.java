/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */

import com.dumbdogdiner.stickyapi.util.textures.TextureHelper;
import com.dumbdogdiner.stickyapi.util.textures.TextureValidator;
import com.dumbdogdiner.stickyapi_tests_common.TestsCommon;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextureValidatorTest {
    @BeforeAll
    static void setUp() {
        TestsCommon.disableHandlers();
        //TestsCommon.addMaskedHandler();

//        // Uncomment the following two lines to see if there are any leaks from OkHTTP
//        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
//        Logger.getLogger(OkHttpClient.class.getName()).addHandler(new StreamHandler(System.err, new SimpleFormatter()));
    }

    @AfterAll
    static void tearDown() {
        //TestsCommon.removeMaskedHandler();
        TestsCommon.enableHandlers();
    }

    @Test
    void validateTexture() {
        assertTrue(TextureValidator.isValidTextureString("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYSJ9fX0="));
    }

    @Test
    void validateTextureBadBase64() {
        String[] tests = {
                // Base64 errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYSJ9fX0===",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh$dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYSJ9fX0=",
        };
        for (String invalidTest : tests) {
            System.out.println(invalidTest);
            assertFalse(TextureValidator.isValidTextureString(invalidTest));
        }
    }

    @Test
    void validateTextureBadHTTP() {
        String[] tests = {
                // HTTP Errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly9jbGllbnRzMy5nb29nbGUuY29tL2dlbmVyYXRlXzIwNCJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly9jbGllbnRzMy5nb29nbGUuY29tL2dlbmVyYXRlXzQwNCJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJbYSJ9fX0=",
        };
        testMultiFail(tests);
    }

    @Test
    void validateTextureBadJSON() {
        String[] tests = {
                // JSON Errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYS",
                "eyJ0ZXh0dXJlcyI6eyJsb2wiOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNWIzZjhjYTRiM2E1NTVjY2IzZDE5NDQ0OTgwOGI0YzlkNzgzMzI3MTk3ODAwZDRkNjU5NzRjYzY4NWFmMmVhIn19fQ==",
                "aGVsbG8="
        };
        testMultiFail(tests);
    }

    @Test
    void validateTextureBadURL() {
        String[] tests = {
                // URL format errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6InNzaDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNWIzZjhjYTRiM2E1NTVjY2IzZDE5NDQ0OTgwOGI0YzlkNzgzMzI3MTk3ODAwZDRkNjU5NzRjYzY4NWFmMmVhIn19fQ==",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6ImZvbyJ9fX0="
        };
        testMultiFail(tests);
    }

    @Test
    void validateTextureBadContentType() {
        String[] tests = {
                // Content type errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly9jYXB0aXZlLmFwcGxlLmNvbS9ob3RzcG90LWRldGVjdC5odG1sIn19fQ==",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vYXBpLmFzaGNvbi5hcHAvbW9qYW5nL3YyL3VzZXIvTm90Y2gifX19"
        };
        testMultiFail(tests);
    }

    @Test
    void validateBadImageType() {
        String[] tests = {
                // Image type errors
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vd3d3LmdzdGF0aWMuY29tL3dlYnAvZ2FsbGVyeS8xLmpwZyJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vd3d3LmdzdGF0aWMuY29tL3dlYnAvZ2FsbGVyeS8xLndlYnAifX19",
        };
        testMultiFail(tests);
    }

    @Test
    void validateBadImageData() {
        String[] tests = {
                // Invalid PNG files
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94czFuMGcwMS5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94czJuMGcwMS5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94czRuMGcwMS5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94czduMGcwMS5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94Y3JuMGcwNC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94bGZuMGcwNC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94YzFuMGcwOC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94YzluMmMwOC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94ZDBuMmMwOC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94ZDNuMmMwOC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94ZDluMmMwOC5wbmcifX19",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly93d3cuc2NoYWlrLmNvbS9wbmdzdWl0ZS94ZHRuMGcwMS5wbmcifX19"
        };
        testMultiFail(tests);
    }


    private void testMultiFail(String[] tests) {
        for (String invalidTest : tests) {
            System.out.println("Now Testing invalid texture " + invalidTest);
            assertFalse(TextureValidator.isValidTextureString(invalidTest));
        }
    }

    @Test
    void getCategories() {
    }

    @Test
    void getTexturesCategory() {
    }

    @Test
    void getTexture() {
        assertEquals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlmMjlmNGFjZDRlNGIyOGVjMGMxYjcyMjU4ZGEzZDM1ZTNiNmE3MWI1Yjk4ZmNlZWFlYjhiYTllMmE2In19fQ==", TextureHelper.getTexture("GEM.AVENTURINE"));
    }

    @Test
    void validateAllTextures() {
        for(String qn : TextureHelper.getQualifiedNames()){
            System.out.println("Testing texture for " + qn + " (" + TextureHelper.getTexture(qn) + ")");
            TextureValidator.validateTextureString(TextureHelper.getTexture(qn));
            assertTrue(TextureValidator.isValidTextureString(TextureHelper.getTexture(qn)));
        }
    }

    @Test
    void getTextures() {
    }
}