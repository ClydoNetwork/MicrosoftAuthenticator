package net.clydo.msa.responses.error;

import com.google.gson.annotations.SerializedName;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an error response from Xbox services.
 *
 * <p>This record includes details about the error, such as an identity identifier, error code, message,
 * and redirect URL. It also provides methods to retrieve the error message, including handling known error codes.</p>
 */
public record XboxError(
        /**
         * The identity associated with the error, often used for tracking or debugging purposes.
         */
        @SerializedName(value = "Identity") String identity,

        /**
         * The error code indicating the specific type of error.
         */
        @SerializedName(value = "XErr") long xErr,

        /**
         * A message describing the error. May be empty or not very descriptive.
         */
        @SerializedName(value = "Message") String message,

        /**
         * A URL to redirect to, if applicable, for further actions or information related to the error.
         */
        @SerializedName(value = "Redirect") String redirect
) implements IError {

    /**
     * Returns a user-friendly message for the error.
     *
     * <p>If the provided message is empty or too short, a known message based on the error code is returned.</p>
     *
     * @return a user-friendly error message.
     */
    @Override
    public String message() {
        if (StringUtils.isEmpty(this.message) || StringUtils.length(this.message) <= 4) {
            return this.knownMessage();
        }
        return this.message;
    }

    /**
     * Provides a known error message based on the error code.
     *
     * <p>This method returns specific messages for well-known error codes. If the error code is not recognized,
     * it returns {@code null}.</p>
     *
     * @return a known error message or {@code null} if the error code is not recognized.
     */
    @Contract(pure = true)
    public @Nullable String knownMessage() {
        val err = Long.toString(this.xErr);
        return switch (err) {
            case "2148916233" -> "The account doesn't have an Xbox account";
            case "2148916235" -> "The account is from a country where Xbox Live is not available/banned";
            case "2148916236", "2148916237" -> "The account needs adult verification on Xbox page. (South Korea)";
            case "2148916238" ->
                    "The account is a child (under 18) and cannot proceed unless the account is added to a Family by an adult";
            default -> null;
        };
    }
}