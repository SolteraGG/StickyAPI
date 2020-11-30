/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.dumbdogdiner.stickyapi.common.util.textures.MobHead;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MobHeadGeneratorTest {
    // No idea how to write test code for this :(
/*
    @Test
    void getHead() {
        ItemMeta foxMeta = new MobHeadGenerator(MobHead.FOX).getHead(33).getItemMeta();
        assert foxMeta instanceof SkullMeta;
        assertEquals(foxMeta.getDisplayName(), MobHead.FOX.getName());
        assertTrue(((SkullMeta) foxMeta).getPlayerProfile().hasTextures());
        assertTrue(((SkullMeta) foxMeta).getPlayerProfile().getProperties().contains(new ProfileProperty("texture", MobHead.FOX.getTexture())));
    }*/
}