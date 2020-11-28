/**
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.protocol;

import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;

import static net.md_5.bungee.protocol.ProtocolConstants.*;


public class Protocol {

    public static HashMap<Integer, Integer> soundEffect = new HashMap<>(){{
        put(MINECRAFT_1_8, 0x29);
        put(MINECRAFT_1_9, 0x47);
        put(MINECRAFT_1_9_1, 0x47);
        put(MINECRAFT_1_9_2, 0x47);
        put(MINECRAFT_1_9_4, 0x46);
        put(MINECRAFT_1_10, 0x46);
        put(MINECRAFT_1_11, 0x46);
        put(MINECRAFT_1_11_1, 0x46);
        put(MINECRAFT_1_12, 0x48);
        put(MINECRAFT_1_12_1, 0x49);
        put(MINECRAFT_1_13, 0x4D);
        put(MINECRAFT_1_13_1, 0x4D);
        put(MINECRAFT_1_13_2, 0x4D);
        put(MINECRAFT_1_14, 0x52);
        put(MINECRAFT_1_14_1, 0x52);
        put(MINECRAFT_1_14_2, 0x52);
        put(MINECRAFT_1_14_3, 0x52);
        put(MINECRAFT_1_14_4, 0x52);
        put(MINECRAFT_1_15, 0x52);
        put(MINECRAFT_1_15_1, 0x52);
        put(MINECRAFT_1_15_2, 0x52);
        put(MINECRAFT_1_16, 0x51);
        put(MINECRAFT_1_16_1, 0x51);
        put(MINECRAFT_1_16_2, 0x51);
        put(MINECRAFT_1_16_3, 0x51);
        put(MINECRAFT_1_16_4, 0x51);
    }};

    /**
     * Get the Sound Effect packet ID
     * 
     * @return {@link Integer}
     * @throws UnsupportedOperationException if the sound id for the current minecraft protocol is not mapped yet (If this is the case, either map the correct ID or get me (Zachery) to do it)
     */
    public static int getSoundEffectId() {
        var protocolVersion = ProxyServer.getInstance().getProtocolVersion();
        var soundEffectId = soundEffect.get(protocolVersion);
        if (soundEffectId == null)
            throw new UnsupportedOperationException(
                    "Minecraft protocol version " + protocolVersion + " has not been implemented yet!");
        return soundEffectId;
    }

}
