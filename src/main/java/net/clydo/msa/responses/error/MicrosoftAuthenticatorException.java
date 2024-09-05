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

import java.io.IOException;
import java.util.concurrent.CompletionException;

/**
 * Exception thrown during Microsoft authentication processes.
 *
 * <p>This exception is used to represent various error scenarios encountered during Microsoft authentication,
 * with support for different types of error messages and causes. It includes a field for holding additional error information
 * conforming to the {@link IError} interface.</p>
 */
public class MicrosoftAuthenticatorException extends CompletionException {
    private final IError error;

    /**
     * Constructs a new {@code MicrosoftAuthenticatorException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public MicrosoftAuthenticatorException(String message, Throwable cause) {
        super(message, cause);
        this.error = () -> message;
    }

    /**
     * Constructs a new {@code MicrosoftAuthenticatorException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public MicrosoftAuthenticatorException(String message) {
        super(message);
        this.error = () -> message;
    }

    /**
     * Constructs a new {@code MicrosoftAuthenticatorException} with a default message for an I/O exception.
     *
     * @param cause the cause of the exception.
     */
    public MicrosoftAuthenticatorException(IOException cause) {
        super("I/O exception thrown during Microsoft HTTP requests", cause);
        this.error = () -> "I/O exception thrown during Microsoft HTTP requests";
    }

    /**
     * Constructs a new {@code MicrosoftAuthenticatorException} with the specified cause and a default message.
     *
     * @param cause the cause of the exception.
     */
    public MicrosoftAuthenticatorException(Throwable cause) {
        super(cause);
        this.error = () -> "Unknown error occurred during Microsoft Authentication";
    }

    /**
     * Constructs a new {@code MicrosoftAuthenticatorException} using an {@link IError} instance.
     *
     * @param error the error instance providing a detailed message.
     */
    public MicrosoftAuthenticatorException(@NotNull IError error) {
        super(error.message());
        this.error = error;
    }

    /**
     * Returns the {@link IError} instance associated with this exception.
     *
     * @return the {@code IError} instance.
     */
    public IError error() {
        return this.error;
    }
}