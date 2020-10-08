/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2019-2020 DumbDogDiner <dumbdogdiner.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *  
 */

package com.dumbdogdiner.stickyapi.common.util;

/**
 * Operations on {@link java.lang.String}
 */
public class StringUtil {

    /**
     * Check if many strings equal a single comparison string
     * 
     * @param haystack the string to compare to
     * @param needles  things that may match the comparison string
     * @return Whether something matches.
     */
    public static boolean compareMany(String haystack, String[] needles) {
        for (String needle : needles) {
            if (haystack.equalsIgnoreCase(needle))
                return true;
        }

        return false;
    }

}