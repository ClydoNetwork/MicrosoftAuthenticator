package net.clydo.msa.responses.success.microsoft;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an XSTS (Xbox Security Token Service) token, including its issuance and expiration details,
 * the token itself, and associated claims.
 *
 * <p>This record includes information about when the token was issued, its expiration time, the token string,
 * and display claims that contain user information.</p>
 */
public record XSTSToken(
        /*
         * The timestamp when the token was issued, in ISO 8601 format.
         */
        @SerializedName(value = "IssueInstant") String issueInstant,

        /*
         * The timestamp when the token will expire, in ISO 8601 format.
         */
        @SerializedName(value = "NotAfter") String notAfter,

        /*
         * The XSTS token string used for authentication and authorization.
         */
        @SerializedName(value = "Token") String token,

        /*
         * The display claims associated with the token, containing user information.
         */
        @SerializedName(value = "DisplayClaims") XSTSClaims displayClaims
) {
    /**
     * Represents the display claims within an XSTS token, including user information.
     *
     * <p>This record includes an array of user information records.</p>
     */
    public record XSTSClaims(
            /*
             * An array of {@link XSTSUserInfo} records containing information about users associated with the token.
             */
            @SerializedName(value = "xui") XSTSUserInfo[] userInfos
    ) {
        /**
         * Represents user information within the XSTS claims.
         *
         * <p>This record includes a unique identifier for the user.</p>
         */
        public record XSTSUserInfo(
                /*
                 * A unique hash representing the user.
                 */
                @SerializedName(value = "uhs") String userHash
        ) {
        }
    }
}