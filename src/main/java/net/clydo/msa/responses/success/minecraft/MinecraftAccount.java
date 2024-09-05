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