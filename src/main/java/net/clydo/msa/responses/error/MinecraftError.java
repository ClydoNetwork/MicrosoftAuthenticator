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
     *
     * <p>This method prioritizes the error message as follows:
     * <ul>
     *   <li>If {@code errorMessage} is not {@code null}, it returns {@code errorMessage}.</li>
     *   <li>Otherwise, if {@code error} is not {@code null}, it returns {@code error}.</li>
     *   <li>If both {@code errorMessage} and {@code error} are {@code null}, it returns {@code path}.</li>
     * </ul>
     * If all fields are {@code null}, the result will be an empty string.</p>
     *
     * @return a comprehensive error message.
     */
    @Override
    public String message() {
        return (this.errorMessage() != null ? this.errorMessage() : (this.error() != null ? this.error() : this.path()));
    }
}