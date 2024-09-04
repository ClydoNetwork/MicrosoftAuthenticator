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