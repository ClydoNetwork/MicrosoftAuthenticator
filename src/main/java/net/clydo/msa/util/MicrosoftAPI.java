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

package net.clydo.msa.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.msa.AuthResult;
import net.clydo.msa.MicrosoftAuthenticator;
import net.clydo.msa.Phase;
import net.clydo.msa.responses.error.MicrosoftAuthenticatorException;
import net.clydo.msa.responses.error.MicrosoftRefreshError;
import net.clydo.msa.responses.error.XboxError;
import net.clydo.msa.responses.success.microsoft.MicrosoftAccount;
import net.clydo.msa.responses.success.microsoft.XBLToken;
import net.clydo.msa.responses.success.microsoft.XSTSToken;
import net.clydo.msa.util.http.HttpUtil;
import net.clydo.msa.util.http.MimeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Utility class for managing interactions with Microsoft's authentication APIs and Xbox Live services.
 * This class provides methods for acquiring and refreshing Microsoft accounts, as well as obtaining Xbox Live (XBL) and XSTS tokens.
 */
@UtilityClass
public class MicrosoftAPI {

    // Client ID for the Microsoft application
    private final String CLIENT_ID = "00000000402B5328";

    // Microsoft and Xbox Live API endpoints for various authentication-related tasks
    //public final String MICROSOFT_TOKEN_ENDPOINT = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token";
    public final String MICROSOFT_AUTHORIZATION_ENDPOINT = "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize";
    public final String MICROSOFT_REDIRECTION_ENDPOINT = "https://login.live.com/oauth20_desktop.srf";
    public final String XBOX_LIVE_SERVICE_SCOPE = "service::user.auth.xboxlive.com::MBI_SSL";
    public final String MICROSOFT_TOKEN_ENDPOINT = "https://login.live.com/oauth20_token.srf";
    public final String XBOX_LIVE_AUTHORIZATION_ENDPOINT = "https://user.auth.xboxlive.com/user/authenticate";
    public final String XSTS_AUTHORIZATION_ENDPOINT = "https://xsts.auth.xboxlive.com/xsts/authorize";

    /**
     * Constructs the Microsoft authorization URL for initiating the OAuth2 authorization flow.
     *
     * @return The constructed authorization URL as a String.
     */
    public String authorizeUrl() {
        return URLUtil.addQueryParams(
                MICROSOFT_AUTHORIZATION_ENDPOINT,
                Map.of(
                        "client_id", CLIENT_ID,
                        "redirect_uri", MICROSOFT_REDIRECTION_ENDPOINT,
                        "scope", XBOX_LIVE_SERVICE_SCOPE,
                        "response_type", "code",
                        "prompt", "login"
                )
        );
    }

    /**
     * Acquires a Microsoft account by exchanging an authorization code for an access token.
     *
     * @param authenticator The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder The builder for constructing the authentication result.
     * @param code          The authorization code obtained from the authorization URL.
     * @return A {@link MicrosoftAccount} representing the acquired Microsoft account information.
     * @throws MicrosoftAuthenticatorException if the token acquisition fails.
     */
    public MicrosoftAccount acquireMicrosoftAccount(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String code
    ) {
        // Set the phase to acquiring Microsoft account
        authenticator.setPhase(Phase.ACQUIRE_MICROSOFT_ACCOUNT);

        // Send a POST request to the Microsoft token endpoint
        val microsoftAccount = HttpUtil.POST(
                MicrosoftAPI.MICROSOFT_TOKEN_ENDPOINT,
                MimeType.APPLICATION_FORM,
                MimeType.WILDCARD,
                Map.of(
                        "client_id", CLIENT_ID,
                        "redirect_uri", MICROSOFT_REDIRECTION_ENDPOINT,
                        "scope", XBOX_LIVE_SERVICE_SCOPE,
                        "code", code,
                        "grant_type", "authorization_code"
                ),
                MicrosoftAccount.class,
                MicrosoftRefreshError.class
        );

        // Set the acquired Microsoft account in the result builder
        resultBuilder.microsoftAccount(microsoftAccount);

        return microsoftAccount;
    }

    /**
     * Refreshes the Microsoft account by using a refresh token to obtain a new access token.
     *
     * @param authenticator The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder The builder for constructing the authentication result.
     * @param refreshToken  The refresh token used to obtain a new access token.
     * @return A {@link MicrosoftAccount} representing the refreshed Microsoft account information.
     * @throws MicrosoftAuthenticatorException if the token refresh fails.
     */
    public MicrosoftAccount refreshToken(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String refreshToken
    ) {
        // Set the phase to acquiring Microsoft account
        authenticator.setPhase(Phase.ACQUIRE_MICROSOFT_ACCOUNT);

        // Send a POST request to the Microsoft token endpoint for refreshing the token
        val microsoftAccount = HttpUtil.POST(
                MicrosoftAPI.MICROSOFT_TOKEN_ENDPOINT,
                MimeType.APPLICATION_FORM,
                MimeType.WILDCARD,
                Map.of(
                        "client_id", CLIENT_ID,
                        "scope", XBOX_LIVE_SERVICE_SCOPE,
                        "refresh_token", refreshToken,
                        "grant_type", "refresh_token"
                ),
                MicrosoftAccount.class,
                MicrosoftRefreshError.class
        );

        // Set the refreshed Microsoft account in the result builder
        resultBuilder.microsoftAccount(microsoftAccount);

        return microsoftAccount;
    }

    /**
     * Acquires an Xbox Live (XBL) token using the Microsoft access token.
     *
     * @param authenticator The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder The builder for constructing the authentication result.
     * @param accessToken   The Microsoft access token used to obtain the XBL token.
     * @return An {@link XBLToken} representing the acquired Xbox Live token.
     * @throws MicrosoftAuthenticatorException if the XBL token acquisition fails.
     */
    public XBLToken acquireXBLToken(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String accessToken
    ) {
        // Set the phase to acquiring XBL token
        authenticator.setPhase(Phase.ACQUIRE_XBL_TOKEN);

        // Send a POST request to the Xbox Live authorization endpoint
        val xblToken = HttpUtil.POST(
                MicrosoftAPI.XBOX_LIVE_AUTHORIZATION_ENDPOINT,
                MimeType.APPLICATION_JSON,
                MimeType.APPLICATION_JSON,
                Map.of(
                        "Properties", Map.of(
                                "AuthMethod", "RPS",
                                "SiteName", "user.auth.xboxlive.com",
                                "RpsTicket", accessToken
                        ),
                        "RelyingParty", "http://auth.xboxlive.com",
                        "TokenType", "JWT"
                ),
                XBLToken.class,
                XboxError.class
        );

        // Set the acquired XBL token in the result builder
        resultBuilder.xblToken(xblToken);

        return xblToken;
    }

    /**
     * Acquires an XSTS token using the XBL token.
     *
     * @param authenticator The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder The builder for constructing the authentication result.
     * @param xblToken      The XBL token used to obtain the XSTS token.
     * @return An {@link XSTSToken} representing the acquired XSTS token.
     * @throws MicrosoftAuthenticatorException if the XSTS token acquisition fails.
     */
    public XSTSToken acquireXSTSToken(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String xblToken
    ) {
        return acquireXSTSToken(authenticator, resultBuilder, xblToken, "rp://api.minecraftservices.com/");
    }

    public XSTSToken acquireXSTSToken(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String xblToken,
            String relyingParty
    ) {
        // Set the phase to acquiring XSTS token
        authenticator.setPhase(Phase.ACQUIRE_XSTS_TOKEN);

        // Send a POST request to the XSTS authorization endpoint
        val xstsToken = HttpUtil.POST(
                MicrosoftAPI.XSTS_AUTHORIZATION_ENDPOINT,
                MimeType.APPLICATION_JSON,
                MimeType.APPLICATION_JSON,
                Map.of(
                        "Properties", Map.of(
                                "SandboxId", "RETAIL",
                                "UserTokens", List.of(xblToken)
                        ),
                        "RelyingParty", relyingParty,
                        "TokenType", "JWT"
                ),
                XSTSToken.class,
                XboxError.class
        );

        // Set the acquired XSTS token in the result builder
        resultBuilder.xstsToken(xstsToken);

        return xstsToken;
    }
}
