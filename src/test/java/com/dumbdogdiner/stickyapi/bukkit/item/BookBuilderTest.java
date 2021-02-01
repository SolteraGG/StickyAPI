/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.item;

import org.junit.jupiter.api.Test;

public class BookBuilderTest {
    @Test
    public void testJsonBuild(){
        BookAndQuillBuilder bb = new BookAndQuillBuilder();
        bb.addPage("Page1 is just a page\\n");
        bb.addPages("And another page", "and yet another");
        //noinspection RedundantArrayCreation
        bb.addPages(new String [] {"yet more", "and more still"});

        System.out.println("Pages NBT:");
        System.out.println(bb.generatePagesNBT());
    }
}
