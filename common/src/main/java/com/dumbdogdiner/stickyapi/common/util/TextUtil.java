/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.common.util;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

public class TextUtil {
    static HashMap<Character, Integer> characterWidths = new HashMap<>();

    static {
        characterWidths.put('!', 1);
        characterWidths.put(',', 1);
        characterWidths.put('\'', 1);
        characterWidths.put('.', 1);
        characterWidths.put(':', 1);
        characterWidths.put(';', 1);
        characterWidths.put('i', 1);
        characterWidths.put('|', 1);
        characterWidths.put('!', 1);

        characterWidths.put('`', 2);
        characterWidths.put('l', 2);

        characterWidths.put(' ', 3);
        characterWidths.put('(', 3);
        characterWidths.put(')', 3);
        characterWidths.put('*', 3);
        characterWidths.put('I', 3);
        characterWidths.put('[', 3);
        characterWidths.put(']', 3);
        characterWidths.put('t', 3);
        characterWidths.put('{', 3);
        characterWidths.put('}', 3);

        characterWidths.put('<', 4);
        characterWidths.put('>', 4);
        characterWidths.put('f', 4);
        characterWidths.put('k', 4);

        characterWidths.put('@', 6);
        characterWidths.put('~', 6);
    }

    // Uses info from: https://minecraft.gamepedia.com/Language#Font
    public static int getCharacterWidth(@NotNull char c) {
        if (c < 32 || c > 126) {
            // Not presently implemented, would require rendering TTF
            return -1;
        }
        if (characterWidths.containsKey(c)) {
            return characterWidths.get(c);
        }
        return 5;
    }
}
