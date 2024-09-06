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
import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.msa.MicrosoftAuthenticator;
import net.clydo.msa.responses.error.IError;
import net.clydo.msa.responses.error.MicrosoftAuthenticatorException;
import net.clydo.msa.util.URLUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for making HTTP requests and handling responses in a standardized way.
 * Provides methods for sending GET and POST requests with error handling and response serialization.
 */
@UtilityClass
public class HttpUtil {


    // Default HTTP client with a connection timeout
    private static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Sends an HTTP GET request to the specified URL with the provided access token for authorization.
     *
     * @param url          The URL to send the GET request to.
     * @param accessToken  The access token to include in the Authorization header.
     * @param successClass The class of the expected successful response body.
     * @param errorClass   The class of the expected error response body.
     * @param <V>          The type of the successful response.
     * @param <E>          The type of the error response.
     * @return The deserialized response body of the successful response.
     * @throws MicrosoftAuthenticatorException if the request fails or an error response is received.
     */
    public <V, E extends IError> V GET(
            String url,
            String accessToken,
            Class<V> successClass,
            Class<E> errorClass
    ) {
        return handleThrowable(() -> {
            val uri = URI.create(url);
            val requestBuilder = HttpRequest.newBuilder(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .GET();

            val request = requestBuilder.build();
            val response = DEFAULT_HTTP_CLIENT.send(request, new GsonBodyHandler());
            return serializeResponse(response, successClass, errorClass);
        });
    }

    /**
     * Sends an HTTP POST request to the specified URL with the provided data, content type, and accept type.
     *
     * @param url          The URL to send the POST request to.
     * @param contentType  The MIME type of the request body.
     * @param acceptType   The expected MIME type of the response body.
     * @param data         The data to be included in the request body.
     * @param successClass The class of the expected successful response body.
     * @param errorClass   The class of the expected error response body.
     * @param <V>          The type of the successful response.
     * @param <E>          The type of the error response.
     * @return The deserialized response body of the successful response.
     * @throws MicrosoftAuthenticatorException if the request fails or an error response is received.
     */
    public <V, E extends IError> V POST(
            String url,
            @NotNull MimeType contentType,
            @NotNull MimeType acceptType,
            Map<Object, Object> data,
            Class<V> successClass,
            Class<E> errorClass
    ) {
        return handleThrowable(() -> {
            val uri = URI.create(url);

            val requestBuilder = HttpRequest.newBuilder(uri)
                    .header("Content-Type", contentType.toString())
                    .header("X-Xbl-Contract-Version", "2")
                    .header("Accept", acceptType.toString());

            val bodyPublisher = createBodyPublisher(contentType, data);
            requestBuilder.POST(bodyPublisher);

            val request = requestBuilder.build();
            val response = DEFAULT_HTTP_CLIENT.send(request, new GsonBodyHandler());
            return serializeResponse(response, successClass, errorClass);
        });
    }

    /**
     * Handles errors that may occur during the execution of a request.
     *
     * @param holder The {@link ThrowableHolder} containing the request logic.
     * @param <R>    The type of the expected response.
     * @return The response if the request is successful.
     * @throws MicrosoftAuthenticatorException if an error occurs during the request.
     */
    public <R> R handleThrowable(ThrowableHolder<R> holder) {
        try {
            return holder.get();
        } catch (Throwable throwable) {
            if (throwable instanceof MicrosoftAuthenticatorException exception) {
                throw exception;
            } else if (throwable instanceof HttpTimeoutException timeoutException) {
                throw new MicrosoftAuthenticatorException("Request timed out", timeoutException);
            } else if (throwable instanceof IOException ioException) {
                throw new MicrosoftAuthenticatorException(ioException);
            } else {
                throw new MicrosoftAuthenticatorException(throwable);
            }
        }
    }

    /**
     * Creates a body publisher for the POST request based on the content type.
     *
     * @param contentType The MIME type of the request body.
     * @param data        The data to be included in the request body.
     * @return The {@link HttpRequest.BodyPublisher} for the request.
     * @throws IllegalArgumentException if the content type is unsupported.
     */
    private HttpRequest.BodyPublisher createBodyPublisher(
            @NotNull MimeType contentType,
            @NotNull Map<Object, Object> data
    ) {
        if (contentType == MimeType.APPLICATION_JSON) {
            return buildJsonBody(data);
        } else if (contentType == MimeType.APPLICATION_FORM) {
            return buildParamsBody(data);
        } else {
            throw new IllegalArgumentException("Unsupported content type: " + contentType);
        }
    }

    /**
     * Builds a body publisher for application/x-www-form-urlencoded data.
     *
     * @param data The data to be included in the request body.
     * @return The {@link HttpRequest.BodyPublisher} for the request.
     */
    public HttpRequest.BodyPublisher buildParamsBody(@NotNull Map<Object, Object> data) {
        val paramsMap = data.entrySet().stream()
                .filter(e -> e.getKey() instanceof CharSequence && e.getValue() instanceof CharSequence)
                .collect(Collectors.toMap(
                        e -> (CharSequence) e.getKey(),
                        e -> (CharSequence) e.getValue(),
                        (existing, replacement) -> existing, // merge function to handle duplicates
                        HashMap::new // supplier for the resulting HashMap
                ));

        return HttpRequest.BodyPublishers.ofString(URLUtil.buildParams(paramsMap));
    }

    /**
     * Builds a body publisher for JSON data.
     *
     * @param data The data to be included in the request body.
     * @return The {@link HttpRequest.BodyPublisher} for the request.
     */
    @Contract("_ -> new")
    public static HttpRequest.@NotNull BodyPublisher buildJsonBody(@NotNull Map<Object, Object> data) {
        return HttpRequest.BodyPublishers.ofString(MicrosoftAuthenticator.gson().toJson(data));
    }

    /**
     * Serializes the HTTP response to the expected response class.
     *
     * @param response   The HTTP response received from the server.
     * @param valueClass The class of the expected successful response body.
     * @param errorClass The class of the expected error response body.
     * @param <V>        The type of the successful response.
     * @param <E>        The type of the error response.
     * @return The deserialized response body of the successful response.
     * @throws MicrosoftAuthenticatorException if an error response is received.
     */
    private <V, E extends IError> V serializeResponse(@NotNull HttpResponse<JsonElement> response, Class<V> valueClass, Class<E> errorClass) throws MicrosoftAuthenticatorException {
        if (response.statusCode() >= 200 && response.statusCode() <= 299) {
            return MicrosoftAuthenticator.gson().fromJson(response.body(), valueClass);
        }

        val error = MicrosoftAuthenticator.gson().fromJson(response.body(), errorClass);
        if (error != null) {
            throw new MicrosoftAuthenticatorException(error);
        } else {
            throw new MicrosoftAuthenticatorException("Occurred unknown error with status code " + response.statusCode() + " for " + valueClass.getSimpleName());
        }
    }
}