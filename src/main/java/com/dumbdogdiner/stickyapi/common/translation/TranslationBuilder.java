package com.dumbdogdiner.stickyapi.common.translation;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Builds translations.
 */
public class TranslationBuilder implements Cloneable {
    /**
     * The locale provider used by this builder.
     */
    @Getter
    private final LocaleProvider provider;

    /**
     * A hashmap of variables
     */
    @Getter
    private final HashMap<String, String> variables = new HashMap<>();

    /**
     * The color character to use during translation.
     */
    @Getter
    @Setter
    private String colorChar = "&";

    /**
     * Construct a new translation builder.
     * @param provider The provider to use for translation
     */
    public TranslationBuilder(LocaleProvider provider) {
        this.provider = provider;
    }

    /**
     * Clone this translation builder.
     * @return {@link TranslationBuilder}
     */
    public TranslationBuilder clone() {
        try {
            return (TranslationBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /**
     * Place a variable into this builder.
     * @param key The key of the variable
     * @param value THe value of the variable
     * @return {@link TranslationBuilder}
     */
    TranslationBuilder put(String key, String value) {
        this.variables.put(key, value);
        return this;
    }

    /**
     * Translate the specified translation node
     * @param node The node to translate
     * @return {@link String}
     */
    String translate(String node) {
        return this.get(this.provider.get(node));
    }

    /**
     * Translate a message
     * @param message The message to translate
     * @return {@link String}
     */
    String get(String message) {
        return Translation.translate(this.provider, message, this.colorChar, this.variables);
    }
}
