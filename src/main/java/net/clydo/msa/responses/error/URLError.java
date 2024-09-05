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

import org.jetbrains.annotations.NotNull;

/**
 * Represents an error related to a URL operation, including an error type and description.
 *
 * <p>This record encapsulates information about the error, including a brief error type and a detailed description.
 * It implements the {@link IError} interface, providing a method to format a comprehensive error message.</p>
 */
public record URLError(
        /*
         * A brief description of the error.
         */
        String error,

        /*
         * A detailed description of the error.
         */
        String description
) implements IError {

    /**
     * Returns a formatted error message combining the error type and description.
     *
     * <p>This method concatenates the error type and description with a colon separator to provide a complete error message.</p>
     *
     * @return a formatted error message.
     */
    @Override
    public @NotNull String message() {
        return this.error() + " : " + this.description();
    }
}