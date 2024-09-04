package net.clydo.msa.responses.success.minecraft;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Minecraft account with authentication details.
 *
 * <p>This record contains information about the account's username, access token, token type, and expiration time.</p>
 */
public record MinecraftAccount(
        /*
         * The username associated with the Minecraft account.
         */
        @SerializedName(value = "username") String username,

        /*
         * The access token used for authenticating requests on behalf of the Minecraft account.
         */
        @SerializedName(value = "access_token") String accessToken,

        /*
         * The type of the access token, typically "Bearer" for OAuth2 tokens.
         */
        @SerializedName(value = "token_type") String tokenType,

        /*
         * The duration in seconds for which the access token is valid before it expires.
         */
        @SerializedName(value = "expires_in") long expiresIn
) {
}