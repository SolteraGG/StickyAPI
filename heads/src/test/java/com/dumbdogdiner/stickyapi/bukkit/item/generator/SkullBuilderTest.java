package com.dumbdogdiner.stickyapi.bukkit.item.generator;/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.dumbdogdiner.stickyapi.mockedplugin.StickyAPIPlugin;
import com.dumbdogdiner.stickyapi.util.textures.TextureHelper;
import com.dumbdogdiner.stickyapi.util.textures.TextureValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import stickyapi.bukkit.item.generator.SkullBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.destroystokyo.paper.profile.ProfileProperty;

import java.net.MalformedURLException;
import java.net.URL;

class SkullBuilderTest {
    static ServerMock svr;

    @BeforeAll
    static void setup() {
        svr = MockBukkit.mock();
        MockBukkit.createMockPlugin();
        //svr.addPlayer();
        MockBukkit.load(StickyAPIPlugin.class);
        //Bukkit.setServer(svr);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    /**
     * For now, we can only test the property setting
     */
    @Test
    void buildSetup() {
        SkullBuilder sb = new SkullBuilder();

        Assertions.assertEquals(sb, sb.category("MHF"));
        Assertions.assertEquals("MHF", sb.getCategory());
//        Assertions.assertEquals(sb, sb.filter("MHFAA"));
//        Assertions.assertEquals("MHF", sb.getFilter());

        Assertions.assertEquals(sb, sb.head("Alex"));
        Assertions.assertEquals("ALEX", sb.getHead());

//        Assertions.assertEquals(sb, sb.head("lmaono"));
//        Assertions.assertEquals("MHF_ALEX", sb.getHead());

        Assertions.assertEquals(sb, sb.quantity(5));
        Assertions.assertEquals(5, sb.getQuantity());
//        Assertions.assertEquals(sb, sb.quantity(99));
//        Assertions.assertEquals(5, sb.getQuantity());
//        Assertions.assertEquals(sb, sb.quantity(-1));
//        Assertions.assertEquals(5, sb.getQuantity());

        Assertions.assertEquals(sb, sb.name("alexa"));
        Assertions.assertEquals("alexa", sb.name());

        try {
            sb.texture(new URL("http://textures.minecraft.net/texture/83cee5ca6afcdb171285aa00e8049c297b2dbeba0efb8ff970a5677a1b644032"));
        } catch (MalformedURLException e) {
            Assertions.fail(e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * If mocked paper is ever available, this can be used, and thusly should not be ignored
     */
    @Disabled("Mocked paper isn't a thing yet")
    @Test
    void build() {

        ItemStack head = new SkullBuilder().category("MHF").head("MHF_Alex").name("alexa").quantity(5).build();
        Assertions.assertEquals(5, head.getAmount());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        Assertions.assertEquals(Material.PLAYER_HEAD, head.getType());
        Assertions.assertTrue(meta.getPlayerProfile().hasTextures());
        for (ProfileProperty pp : meta.getPlayerProfile().getProperties()) {
            if (pp.getName().equals("texture")) {
                Assertions.assertEquals(TextureHelper.getTexture("MHF.MHF_Alex"), pp.getValue());
                Assertions.assertTrue(TextureValidator.isValidTextureString(pp.getValue()));
            }
        }
        Assertions.assertEquals("alexa", meta.getDisplayName());

    }
}