/*
 * This file is part of MicrosoftAuthenticator.
 *
 * MicrosoftAuthenticator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * MicrosoftAuthenticator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MicrosoftAuthenticator.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2024 ClydoNetwork
 */

package net.clydo.msa.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.msa.responses.error.MicrosoftAuthenticatorException;
import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for handling URL-related operations such as adding query parameters,
 * encoding and decoding values, and retrieving specific query parameters from URLs.
 */
@UtilityClass
public class URLUtil {

    /**
     * Adds query parameters to a given URL.
     *
     * @param url    The base URL to which the parameters will be added.
     * @param params A map containing the query parameters and their corresponding values.
     * @return The URL with the query parameters appended.
     */
    public String addQueryParams(@NotNull String url, @NotNull Map<CharSequence, CharSequence> params) {
        if (params.isEmpty()) {
            return url;
        }

        val queryParamString = buildParams(params);
        return url.contains("?") ? url + "&" + queryParamString : url + "?" + queryParamString;
    }

    /**
     * Builds a query string from a map of parameters.
     *
     * @param params A map of parameters where the key is the parameter name and the value is the parameter value.
     * @return A string representing the encoded query parameters.
     */
    public String buildParams(@NotNull Map<CharSequence, CharSequence> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
     * Retrieves the value of a specific query parameter from a URL.
     *
     * @param url       The URL containing the query parameters.
     * @param paramName The name of the parameter whose value is to be retrieved.
     * @return The decoded value of the specified query parameter, or null if the parameter is not found.
     * @throws MicrosoftAuthenticatorException if there is an issue with the URL or parameter retrieval.
     */
    public String getQueryParam(@NotNull String url, @NotNull String paramName) throws MicrosoftAuthenticatorException {
        val pattern = Pattern.compile(Pattern.quote(paramName) + "=([^&]*)");
        val matcher = pattern.matcher(url);
        if (matcher.find()) {
            return decode(matcher.group(1));
        }
        return null;
    }

    /**
     * Encodes a given value for safe transmission in a URL.
     *
     * @param value The value to be encoded.
     * @return The encoded value as a string.
     */
    private String encode(@NotNull CharSequence value) {
        return URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Decodes a given value that was encoded for a URL.
     *
     * @param value The encoded value to be decoded.
     * @return The decoded value as a string.
     */
    private String decode(@NotNull String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
