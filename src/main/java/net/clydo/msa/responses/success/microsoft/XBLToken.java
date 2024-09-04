package net.clydo.msa.responses.success.microsoft;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an XBL (Xbox Live) token, including its issuance and expiration details,
 * the token itself, and associated claims.
 *
 * <p>This record contains information about when the token was issued, its expiration time, the token string,
 * and display claims related to Xbox Live user information.</p>
 */
public record XBLToken(
        /*
         * The timestamp when the token was issued, in ISO 8601 format.
         */
        @SerializedName(value = "IssueInstant") String issueInstant,

        /*
         * The timestamp when the token will expire, in ISO 8601 format.
         */
        @SerializedName(value = "NotAfter") String notAfter,

        /*
         * The XBL token string used for authentication and authorization.
         */
        @SerializedName(value = "Token") String token,

        /*
         * The display claims associated with the token, containing Xbox Live user information.
         */
        @SerializedName(value = "DisplayClaims") XBLClaims displayClaims
) {
    /**
     * Represents the display claims within an XBL token, including Xbox Live user information.
     *
     * <p>This record includes an array of user information records.</p>
     */
    public record XBLClaims(
            /*
             * An array of {@link XBLUserInfo} records containing information about Xbox Live users associated with the token.
             */
            @SerializedName(value = "xui") XBLUserInfo[] userInfos
    ) {
        /**
         * Represents user information within the XBL claims.
         *
         * <p>This record includes a unique identifier for the Xbox Live user.</p>
         */
        public record XBLUserInfo(
                /*
                 * A unique hash representing the Xbox Live user.
                 */
                @SerializedName(value = "uhs") String userHash
        ) {
        }
    }
}