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