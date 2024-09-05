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
 * Represents an error that occurs during the Microsoft refresh token process.
 *
 * <p>This record contains information about the error, including a brief error code, a detailed description of the error,
 * and a correlation ID for tracking requests across systems.</p>
 */
public record MicrosoftRefreshError(
        /*
         * A brief code or identifier for the error that occurred.
         */
        @SerializedName(value = "error") String error,

        /*
         * A detailed description of the error, providing more context or explanation.
         */
        @SerializedName(value = "error_description") String errorDescription,

        /*
         * A unique identifier used to track the request or error across different systems for diagnostic purposes.
         */
        @SerializedName(value = "correlation_id") String correlationID
) implements IError {

    /**
     * Returns a detailed error description.
     *
     * <p>This method returns the {@code errorDescription} field, which provides a comprehensive explanation of the error.</p>
     *
     * @return a detailed description of the error.
     */
    @Override
    public String message() {
        return this.errorDescription();
    }
}