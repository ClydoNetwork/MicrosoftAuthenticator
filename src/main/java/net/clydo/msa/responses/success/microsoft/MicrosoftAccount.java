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

package net.clydo.msa.responses.success.microsoft;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Microsoft account with authentication details, including access and refresh tokens,
 * token type, expiration information, and user details.
 *
 * <p>This record contains information about the type of token, its expiration time, the scope of the token,
 * and both access and refresh tokens, as well as the user ID associated with the account.</p>
 */
public record MicrosoftAccount(
        /*
         * The type of the token, typically "Bearer" for OAuth2 tokens.
         */
        @SerializedName(value = "token_type") String tokenType,

        /*
         * The duration in seconds for which the access token is valid before it expires.
         */
        @SerializedName(value = "expires_in") long expiresIn,

        /*
         * The scope of the token, indicating the access permissions granted.
         */
        @SerializedName(value = "scope") String scope,

        /*
         * The access token used for authenticating requests on behalf of the Microsoft account.
         */
        @SerializedName(value = "access_token") String accessToken,

        /*
         * The refresh token used to obtain a new access token when the current one expires.
         */
        @SerializedName(value = "refresh_token") String refreshToken,

        /*
         * The unique identifier of the Microsoft account user.
         */
        @SerializedName(value = "user_id") String userId
) {
}