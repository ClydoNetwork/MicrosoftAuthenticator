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

package net.clydo.msa.responses.error;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an error related to Minecraft, including details about the error's origin and various messages.
 *
 * <p>This record holds information about the error's path, type, and detailed messages, including a specific error message
 * and a message intended for developers. It implements the {@link IError} interface and provides a method to retrieve
 * a comprehensive error message.</p>
 */
public record MinecraftError(
        /*
         * The path or endpoint where the error occurred.
         */
        @SerializedName(value = "path") String path,

        /*
         * The type or category of the error.
         */
        @SerializedName(value = "errorType") String errorType,

        /*
         * A brief description or code for the error.
         */
        @SerializedName(value = "error") String error,

        /*
         * A detailed error message describing the issue.
         */
        @SerializedName(value = "errorMessage") String errorMessage,

        /*
         * A message intended for developers, potentially containing more technical details.
         */
        @SerializedName(value = "developerMessage") String developerMessage
) implements IError {

    /**
     * Returns a comprehensive error message based on the available error information.
     * This method prioritizes the error message as follows:
     * <ul>
     *   <li>If {@code errorMessage} is not {@code null}, it returns {@code errorMessage}.</li>
     *   <li>Otherwise, if {@code error} is not {@code null}, it returns {@code error}.</li>
     *   <li>If both {@code errorMessage} and {@code error} are {@code null}, it returns {@code path}.</li>
     * </ul>
     * If all fields are {@code null}, the result will be an empty string.
     *
     * @return a comprehensive error message.
     */
    @Override
    public String message() {
        return (this.errorMessage() != null ? this.errorMessage() : (this.error() != null ? this.error() : this.path()));
    }
}