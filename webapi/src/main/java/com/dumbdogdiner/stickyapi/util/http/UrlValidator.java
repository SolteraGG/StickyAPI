/*
 * Copyright (c) 2020-2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.util.http;

import lombok.Getter;
import okhttp3.HttpUrl;

import java.net.URL;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * A URL Validator, based on <a href="https://owasp.org/www-community/OWASP_Validation_Regex_Repository">OWASP's URL
 * regex</a>, with minor tweaks to remove unsupported protocols and allow a domain to be specified
 */
public class UrlValidator {
    /**
     * The regex used by default to test if a domain is valid
     */
    public static final Pattern DEFAULT_DOMAIN_REGEX = Pattern.compile("(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+");

    @Getter
    private static final UrlValidator instance = new UrlValidator();

    /**
     * The regex used by default to test if URLs are valid
     */
    @SuppressWarnings("unused")
    public static final Pattern DEFAULT_URL_REGEX = instance.urlRegex;

    @Getter
    private final Pattern urlRegex;

    private UrlValidator() {
        this(DEFAULT_DOMAIN_REGEX);
    }

    private UrlValidator(Pattern domain){
        urlRegex = Pattern.compile("^(((https?)://)" + domain.pattern() + ")([).!';/?:,]])?$");
    }

    /**
     * Constructs a {@link UrlValidator} that matches against a specific domain RegEx
     * @param domainMatchPattern a RegEx pattern that is used as the sub-pattern to validate the domain section of a URL
     * @return An instance of the new {@link UrlValidator}
     */
    public static UrlValidator withDomain(Pattern domainMatchPattern){
        return new UrlValidator(domainMatchPattern);
    }

    /**
     * Constructs a {@link UrlValidator} that matches against multiple specified domains
     * @param domains The domains to match. "*." indicates wildcard subdomains are allowed
     * @return An instance of the new {@link UrlValidator}
     */
    public static UrlValidator withDomains(String ... domains){
        StringJoiner domainRegex = new StringJoiner(")|(","((","))");
        for(String domain : domains){
            domainRegex.add(domain.replace("*.", "("+UrlValidator.DEFAULT_DOMAIN_REGEX.pattern() + "\\.)?"));
        }
        return new UrlValidator(Pattern.compile(domainRegex.toString()));
    }

    public boolean isValid(String url){
        if(this != instance && !instance.isValid(url))
            return false;
        return urlRegex.matcher(url).matches();
    }

    public boolean isValid(URL url) {
        return isValid(url.toExternalForm());
    }

    public boolean isValid(HttpUrl url) {
        return isValid(url.url());
    }
}
