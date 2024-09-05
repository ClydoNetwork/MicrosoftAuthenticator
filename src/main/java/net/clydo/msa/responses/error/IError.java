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

/**
 * Represents an error with a message.
 *
 * <p>This interface defines a contract for classes that represent errors with a method to retrieve a descriptive
 * error message. Implementations of this interface should provide the actual error message that describes the error.</p>
 */
public interface IError {

    /**
     * Returns a descriptive error message.
     *
     * <p>Implementations of this interface should return a string that provides details about the error, which
     * can be used for logging, displaying to users, or other error handling purposes.</p>
     *
     * @return a string containing the error message.
     */
    String message();
}