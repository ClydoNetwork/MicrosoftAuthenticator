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

package net.clydo.msa.util.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.clydo.msa.MicrosoftAuthenticator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * A custom body handler for processing HTTP responses with a JSON body.
 * This handler converts the response body input stream into a {@link JsonElement} using Gson.
 */
public class GsonBodyHandler implements HttpResponse.BodyHandler<JsonElement> {

    /**
     * Creates a {@link HttpResponse.BodySubscriber} that processes the response body as an input stream
     * and maps it to a {@link JsonElement}.
     *
     * @param responseInfo The response metadata.
     * @return A {@link HttpResponse.BodySubscriber} that converts the response body to a {@link JsonElement}.
     */
    @Override
    public HttpResponse.BodySubscriber<JsonElement> apply(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofInputStream(),
                inputStream -> {
                    try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                        // Parse the input stream into a JsonElement using Gson
                        return MicrosoftAuthenticator.gson().fromJson(reader, JsonElement.class);
                    } catch (JsonSyntaxException | IOException e) {
                        // Handle JSON parsing errors or IOExceptions
                        throw new RuntimeException("Failed to parse JSON", e);
                    }
                }
        );
    }
}
