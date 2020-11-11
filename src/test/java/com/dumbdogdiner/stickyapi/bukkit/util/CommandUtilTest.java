/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the GPLv3 license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bukkit.util;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertFalse;

import static org.mockito.Mockito.*;


import org.powermock.modules.junit4.rule.PowerMockRule;

@TestInstance(Lifecycle.PER_CLASS)
public class CommandUtilTest {
    @Rule
    PowerMockRule rule = new PowerMockRule();

    private Server server;
    private Command command;

    @BeforeAll
    public void setup() throws NoSuchFieldException {
        server = mock(Server.class);
        command = mock(Command.class);
    }

    // Successful test currently not possible without API changes.

    @Test
    public void testRegisterCommandFailure() {
        // Return a null commandMap, expect false.
        assertFalse(CommandUtil.registerCommand(server, command));
    }
}
