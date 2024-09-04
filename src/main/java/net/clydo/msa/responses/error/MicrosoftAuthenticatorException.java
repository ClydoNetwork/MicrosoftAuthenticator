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