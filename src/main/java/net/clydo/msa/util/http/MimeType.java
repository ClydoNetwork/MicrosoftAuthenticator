package net.clydo.msa.util.http;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing common MIME types used in HTTP requests and responses.
 * Provides a way to standardize and work with MIME types in a type-safe manner.
 */
@Getter
public enum MimeType {
    /**
     * MIME type for JSON content.
     */
    APPLICATION_JSON("application/json"),

    /**
     * MIME type for form data submitted via HTTP POST requests.
     */
    APPLICATION_FORM("application/x-www-form-urlencoded"),

    /**
     * Wildcard MIME type representing any media type.
     */
    WILDCARD("*/*");

    /**
     * The string representation of the MIME type.
     */
    private final String mimeType;

    /**
     * Constructor to initialize the MIME type with the provided string value.
     *
     * @param mimeType The string representation of the MIME type.
     */
    MimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Returns the string representation of the MIME type.
     *
     * @return The string representation of the MIME type.
     */
    @Override
    public String toString() {
        return this.mimeType;
    }

    /**
     * Retrieves a {@link MimeType} enum constant corresponding to the given string value.
     *
     * @param value The string representation of the MIME type to match.
     * @return The corresponding {@link MimeType} enum constant.
     * @throws IllegalArgumentException if the value does not match any known MIME type.
     */
    public static @NotNull MimeType fromValue(String value) {
        for (MimeType mimeType : values()) {
            if (mimeType.mimeType.equalsIgnoreCase(value)) {
                return mimeType;
            }
        }
        throw new IllegalArgumentException("Unknown MIME type: " + value);
    }
}
