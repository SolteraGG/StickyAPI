/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
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
 */

package com.dumbdogdiner.stickyapi.common.translation;

import java.lang.Character;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dumbdogdiner.stickyapi.common.util.TimeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * A class for parsing configurations
 */
public class Translation {

    private static final Pattern urlPattern = Pattern.compile("(https:\\/\\/|http:\\/\\/)((?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?");

    /**
     * Sub-class for easier URL formatting
     * between methods.
     */
    public static class URLPair {
        String fullPath;
        String shortened;

        public URLPair(String fullUrl, String shortenedUrl) {
            this.fullPath = fullUrl;
            this.shortened = shortenedUrl;
        }

        public String getShortened() {
            return shortened;
        }

        public String getFullPath() {
            return fullPath;
        }

    }

    // {VARIABLE|pluralize:"y,ies"}
    private static String pluralize(String lvalue, String arg) {
        String singlar = "", plural = (arg.isEmpty() ? "s" : arg);
        if (arg.contains(",")) {
            String values[] = arg.trim().split(",");
            singlar = values[0];
            plural = values[1];
        }

        if (lvalue == "1")
            return singlar;
        else
            return plural;
    }

    private static String yesno(String lvalue, String arg) {
        if (arg.isEmpty())
            return Boolean.valueOf(lvalue) ? "yes" : "no";
        else if (arg.contains(","))
            return arg.split(",")[Boolean.valueOf(lvalue) ? 0 : 1];
        else
            return Boolean.valueOf(lvalue) ? arg : "no";
    }

    /**
     * List of functions which are executed as part of the translation functions.
     * When the placeholder parser begins parsing a string, it will parse the
     * following format as below: {@code {VARIABLE|function:"HH:MM:SS"}} Where
     * `VARIABLE` would be the name of the variables passed to the
     * TranslateVariables function, `function` would be the name of the function in
     * this class to execute, and everything in quotes after the colon would be the
     * 2nd argument passed to the functions in this map.
     * 
     * Each function takes 2 arguments, the first being the dereferenced value of
     * the variable itself as a string and the 2nd being the additional arguments
     * provided in quotes after the colon in the placeholder string. For example a
     * string with `{TimeBanned|datetime:"HH:MM:SS"}` would execute the `datetime`
     * function in the map below as:
     * {@code datetime(Variables.get("TimeBanned"), "HH:MM:SS")}
     */
    public static TreeMap<String, BiFunction<String, String, String>> functions = new TreeMap<String, BiFunction<String, String, String>>(String.CASE_INSENSITIVE_ORDER) {
        {
            put("pluralize", Translation::pluralize);
            put("datetime", (String lvalue, String args) -> lvalue == null || lvalue.equals("") ? "Never"
                    : (new SimpleDateFormat(args)).format(Timestamp.valueOf(lvalue)));
            // FIXME: Allow for timestamps AND longs, for now, use longs!
            put("duration", (String lvalue, String unused) -> lvalue == null || lvalue.equals("") ? "Never" : TimeUtil.durationString(Long.parseLong(lvalue)));
            put("expiry", (String lvalue, String unused) -> lvalue == null || lvalue.equals("") ? "Never" : TimeUtil.expires(Timestamp.valueOf(lvalue)));
            put("cut", (String lvalue, String arg) -> lvalue.replace(arg, ""));
            put("empty_if_false", (String lvalue, String arg) -> Boolean.parseBoolean(lvalue) ? arg : "");
            put("empty_if_true", (String lvalue, String arg) -> Boolean.parseBoolean(lvalue) ? "" : arg);
            put("default_if_none", (String lvalue, String arg) -> lvalue == null ? arg : lvalue);
            put("lower", (String lvalue, String unused) -> lvalue.toLowerCase());
            put("upper", (String lvalue, String unused) -> lvalue.toUpperCase());
            put("yesno", (String lvalue, String arg) -> yesno(lvalue, arg));
        }
    };

    // Java apparently has no capabiliy to do something even a simple language like
    // C can do
    // which is parse hexidecimal numbers and tell me if they're fucking valid. This
    // function
    // will do exactly what I need by checking if the char is A through F, 0 through
    // 9.
    /**
     * Checks if the character is a valid minecraft color code
     * <p>Returns true if the character is valid minecraft colorcode
     * @param ch The character to check for a valid color code char
     * @return {@link java.lang.Boolean}
     * @deprecated Since Minecraft 1.16 supports 32 bit colors, this function will
     *             be deprecated.
     */
    @Deprecated
    public static boolean isxdigit(char ch) {
        // First check if the char is a digit, Java can manage to do this one amazingly.
        if (!Character.isDigit(ch)) {
            // If it's not a number between 0 through 9, check if it's A through F
            // If we are lower case, switch to upper and compare that way.
            if (ch >= 97)
                ch -= 32;

            // Minecraft uses some new special chars for formatting so we have
            // to account for those too.
            if (ch == 'R' || (ch < 80 && ch > 74))
                return true;

            // if they're greater than 70 (aka 'F') but less than 65 (aka 'A')
            // then it's not valid hexidecimal.
            if (ch > 71 || ch < 65)
                return false;
        }

        return true;
    }

    public static final char SPECIAL_CHAR = '\u00A7';

    /**
     * Replace the character sequence in `chars` to swap out with the minecraft
     * color char while also validating that the color code sequence is valid.
     * 
     * <p>Returns a color formatted message for Minecraft clients
     * @param chars   Character sequence to replace with the section character
     *                minecraft uses for color codes
     * @param message Message containing sequences of `chars` in it
     * @return {@link java.lang.String}.
     */
    public static String translateColors(String chars, String message) {
        if (message == null)
            return null;

        if (chars == null)
            return message;

        // Don't allocate if we don't have to.
        if (!message.contains(chars))
            return message;

        StringBuilder retstr = new StringBuilder(message);
        for (int pos = message.indexOf(chars); pos != -1; pos = message.indexOf(chars, pos)) {
            if (pos + 1 > message.length())
                break;

            // Make sure the next char is valid hex as Minecraft uses a hexidecimal number
            if (Translation.isxdigit(message.charAt(pos + 1))) {
                // Now we replace the starting char with our special char.
                retstr.setCharAt(pos, SPECIAL_CHAR);
                pos += 2;
            } else // Skip 2 characters, invalid sequence.
                pos += 2;
        }

        return retstr.toString();
    }

    private static final Pattern interopRegex = Pattern.compile("\\{(.+?)(?:\\|([{}a-z]+)(?::\"(.+?)\")?)?}", Pattern.DOTALL);

    // Used to replace variables inside of strings.
    // {Player} has been banned by {Executioner}: {Reason}
    // {Player has been banned by CONSOLE: fuck you.
    /**
     * Replace all placeholders in a string, executing placeholder functions in the
     * process to format strings with variables provided.
     * 
     * <p>Returns a formatted string with all placeholders from Variables replaced.
     * @param locale    The LocaleProvider context
     * @param message   The message to have placeholders replaced
     * @param variables The variables to be utilized in this message for the
     *                  placeholders and their functions
     * @return {@link java.lang.String}
     */
    public static String translateVariables(LocaleProvider locale, String message, Map<String, String> variables) {
        var matcher = interopRegex.matcher(message);

        // get a list of all matches because it makes so much more sense.
        var matches = new ArrayList<MatchResult>();
        while (matcher.find()) {
            matches.add(matcher.toMatchResult());
        }

        if (matches.size() == 0) {
            return message;
        }

        var out = new StringBuilder();
        out.append(message, 0, matches.get(0).start());

        for (var i = 0; i < matches.size(); i++) {
            var match = matches.get(i);
            var variable = match.group(1);
            var function = functions.get(match.group(2));

            String translatedContent = translateVariables(locale, locale.get(variable), variables);
            if (function != null) {
                translatedContent = function.apply(translatedContent, match.group(3));
            }
            out.append(translatedContent);

            if (i == matches.size() - 1) {
                continue;
            }
            out.append(message, match.end() , matches.get(i + 1).start());
        }

        out.append(message, matches.get(matches.size() - 1).end(), message.length());
        return out.toString();
    }

    /**
     * Translate the preformatted string to a fully formatted string ready for
     * players to see, switching out color codes and placeholders.
     * <p>Returns a string with color sequences and placeholders translated to their
     * formatted message ready for the player.
     * @param locale     The LocaleProvider context
     * @param message    The message containing placeholders and untranslated color
     *                   code sequences
     * @param colorChars The character used as the prefix for color strings
     *                   (bukkit/spigot use `&amp;` and so do we most of the time)
     * @param variables  A list of variables to be parsed by the placeholder
     * @return {@link java.lang.String}
     */
    public static String translate(LocaleProvider locale, String message, String colorChars, Map<String, String> variables) {
        String retstr = Translation.translateVariables(locale, message, variables);
        retstr = Translation.translateColors(colorChars, retstr);
        return retstr;
    }

    /**
     * Find the first URL in a given Text.
     * <p>Returns a URLPair object which stores the full URL
     * as well as a shortened version (e.g. www.github.com)
     * @param text The text that should be checked for URLs
     * @return {@link URLPair}
     */
    public static URLPair findURL(String text) {
        Matcher matcher = urlPattern.matcher(text);

        if(matcher.find()) {
            return new URLPair(matcher.group(0), matcher.group(2));
        }
        return null;
    }

    /**
     * Converts URLs in a preformatted String to clickable
     * JSON components.
     * <p>Returns a TextComponent containing formatted and
     * clickable URLs.
     * @param text The text that should be converted into
     *             a TextComponent with formatted URLs.
     * @return {@link TextComponent}
     */
    public static TextComponent convertURLs(String text) {
        TextComponent finalComp = new TextComponent();
        for(String s : text.split(" ")) {
            URLPair url = findURL(s + " ");
            if((url) == null) {
                finalComp.addExtra(s + " ");
            } else {
                TextComponent urlComponent = new TextComponent(url.getShortened() + " ");
                urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.getFullPath()));
                urlComponent.setBold(true);
                finalComp.addExtra(urlComponent);
            }
        }

        return finalComp;
    }


}
