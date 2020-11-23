/**
 * Copyright (c) 2020 DumbDogDiner <a href="dumbdogdiner.com">&lt;dumbdogdiner.com&gt;</a>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.common.util;

public class TextUtil {
    // Uses info from: https://minecraft.gamepedia.com/Language#Font
    public static int getCharacterWidth(char c){
        if(c < 32 || c > 126){
            // Not presently implemented, would require rendering TTF
            return -1;
        }
        switch(c){
            case '!':
            case ',':
            case '\'':
            case '.':
            case ':':
            case ';':
            case 'i':
            case '|':
                return 1;
            case '`':
            case 'l':
                return 2;
            case ' ':
            case '(':
            case ')':
            case '*':
            case 'I':
            case '[':
            case ']':
            case 't':
            case '{':
            case '}':
                return 3;
            case '<':
            case '>':
            case 'f':
            case 'k':
                return 4;
            case '@':
            case '~':
                return 6;
            default:
                return 5;
        }
    }
}