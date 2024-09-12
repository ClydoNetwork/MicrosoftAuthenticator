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

package net.clydo.msa;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.clydo.msa.frame.WebFrame;
import net.clydo.msa.responses.error.MicrosoftAuthenticatorException;
import net.clydo.msa.util.MicrosoftAPI;
import net.clydo.msa.util.MinecraftAPI;
import net.clydo.msa.util.http.Cookies;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

/**
 * Handles the authentication process with Microsoft and Xbox Live services.
 * This class manages the authentication flow, including opening a webview for login, handling token refresh,
 * and acquiring tokens necessary for Minecraft authentication.
 */
@Setter
@Getter
public class MicrosoftAuthenticator {
    private static final Gson GSON = new Gson();

    public static Gson gson() {
        return GSON;
    }

    // Current phase of the authentication process
    @Nullable
    private Phase phase = null;

    /**
     * Initiates the authentication process by opening a webview for the user to log in.
     *
     * @param authenticatorOptions The options for configuring the login window.
     * @return A CompletableFuture containing the authentication result.
     */
    public CompletableFuture<AuthResult> asyncWebview(AuthenticatorOptions authenticatorOptions) {
        this.setPhase(Phase.OPEN_LOGIN_PAGE);

        val future = new CompletableFuture<AuthResult>();

        // Initialize and clear cookies
        Cookies.getOrCreate();

        // Generate the authorization URL
        val authorizeUrl = MicrosoftAPI.authorizeUrl();

        // Start the webview with the authorization URL
        val webFrame = new AtomicReference<>(new WebFrame(authenticatorOptions));

        CompletableFuture.runAsync(() -> {
            webFrame.get()
                    .start(authorizeUrl)
                    .thenAcceptAsync(code -> {
                        // Acquire Microsoft account and start authentication
                        val authResult = this.syncCode(code);

                        // Complete the authentication process
                        future.complete(authResult);
                    }).exceptionally(throwable -> {
                        // Handle exceptions during the webview process
                        future.completeExceptionally(throwable instanceof MicrosoftAuthenticatorException exception ? exception : throwable);
                        return null;
                    });

            while (true) {
                if ((future.isDone() || future.isCompletedExceptionally() || future.isCancelled()) && (webFrame.get().getWebView() == null))
                    break;
            }

            webFrame.get().close();
            webFrame.set(null);
        });

        return future;
    }

    /**
     * Retrieves the authorization URL used to initiate the Microsoft authentication flow.
     * This URL is used to open a webview for the user to log in with their Microsoft account.
     *
     * @return The Microsoft authorization URL.
     */
    public String getAuthorizeUrl() {
        return MicrosoftAPI.authorizeUrl();
    }

    /**
     * Initiates the authentication process asynchronously using the provided authorization code.
     * The operation is performed asynchronously and returns a CompletableFuture.
     *
     * @param code The authorization code returned from the login page.
     * @return A CompletableFuture containing the authentication result.
     * @throws MicrosoftAuthenticatorException If an error occurs during authentication.
     */
    public CompletableFuture<AuthResult> asyncCode(String code) {
        return CompletableFuture.supplyAsync(() -> {
            // Acquire Microsoft account and start authentication
            // Complete the authentication process
            return this.syncCode(code);
        }).exceptionally(throwable -> {
            // Handle exceptions during the token-based authentication process
            throw throwable instanceof MicrosoftAuthenticatorException ? (MicrosoftAuthenticatorException) throwable : new RuntimeException(throwable);
        });
    }

    /**
     * Performs the authentication process synchronously using the provided authorization code.
     *
     * @param code The authorization code returned from the login page.
     * @return The authentication result containing the user's access tokens and Minecraft profile.
     * @throws MicrosoftAuthenticatorException If an error occurs during authentication.
     */
    public AuthResult syncCode(String code) {
        val resultBuilder = AuthResult.builder();

        val microsoftToken = MicrosoftAPI.acquireMicrosoftAccount(this, resultBuilder, code);
        if (microsoftToken != null) {
            this.authenticateByMSA(resultBuilder, microsoftToken.accessToken());
        }

        return resultBuilder.build();
    }

    /**
     * Initiates the authentication process using a refresh token or an access token, handling token expiration.
     *
     * @param refreshToken The refresh token for obtaining a new access token.
     * @param accessToken  The current access token.
     * @param expiresAt    The expiration time of the current access token.
     * @return A CompletableFuture containing the authentication result.
     */
    public CompletableFuture<AuthResult> asyncToken(String refreshToken, String accessToken, long expiresAt, BooleanSupplier forceRefreshToken) {
        return CompletableFuture.supplyAsync(() -> {
            val resultBuilder = AuthResult.builder();

            var msaAccessToken = accessToken;
            var retryCount = 0;
            var shouldRefreshToken = expired(expiresAt) || forceRefreshToken.getAsBoolean();

            if (!shouldRefreshToken) {
                return null;
            }

            while (true) {
                if (retryCount > 0) {
                    // Refresh the Microsoft account access token
                    val microsoftAccount = MicrosoftAPI.refreshToken(this, resultBuilder, refreshToken);
                    if (microsoftAccount != null) {
                        msaAccessToken = microsoftAccount.accessToken();
                        //System.out.println(msaAccessToken);
                    }
                }

                try {
                    // Authenticate using the Microsoft account access token
                    this.authenticateByMSA(resultBuilder, msaAccessToken);
                    break;
                } catch (Throwable throwable) {
                    // Retry once if an error occurs
                    if (retryCount < 3) {
                        retryCount++;
                    } else {
                        throw throwable;
                    }
                }
            }

            // Return the authentication result
            return resultBuilder.build();
        }).exceptionally(throwable -> {
            // Handle exceptions during the token-based authentication process
            throw throwable instanceof MicrosoftAuthenticatorException ? (MicrosoftAuthenticatorException) throwable : new RuntimeException(throwable);
        });
    }


    /**
     * Handles the core Microsoft and Minecraft authentication process.
     *
     * @param resultBuilder The builder for constructing the authentication result.
     * @param accessToken   The Microsoft access token used for authentication.
     * @throws MicrosoftAuthenticatorException if any step in the authentication process fails.
     */
    private void authenticateByMSA(@NotNull AuthResult.AuthResultBuilder resultBuilder, String accessToken) throws MicrosoftAuthenticatorException {
        // Acquire Xbox Live (XBL) token
        val xblToken = MicrosoftAPI.acquireXBLToken(this, resultBuilder, accessToken);
        if (xblToken == null) {
            return;
        }

        // Acquire XSTS token using the XBL token
        val xstsToken = MicrosoftAPI.acquireXSTSToken(this, resultBuilder, xblToken.token());
        if (xstsToken == null) {
            return;
        }

        // Acquire Minecraft token using the XSTS token
        val minecraftLogin = MinecraftAPI.acquireMinecraftToken(this, resultBuilder, xstsToken.displayClaims().userInfos()[0].userHash(), xstsToken.token());
        if (minecraftLogin == null) {
            return;
        }

        // Acquire Minecraft store information and check if the user owns Minecraft Java Edition
        val minecraftStore = MinecraftAPI.acquireMinecraftStore(this, resultBuilder, minecraftLogin.accessToken());
        if (minecraftStore != null) {
            if (Arrays.stream(minecraftStore.items()).noneMatch(item -> item.name().equals("game_minecraft"))) {
                throw new MicrosoftAuthenticatorException("Player didn't buy Minecraft Java Edition or did not migrate its account");
            }
        }

        // Acquire Minecraft profile information
        MinecraftAPI.acquireMinecraftProfile(this, resultBuilder, minecraftLogin.accessToken());
    }

    /**
     * Converts the expiration time from seconds to the corresponding epoch time.
     *
     * @param expiresIn The number of seconds until expiration.
     * @return The expiration time as an epoch second.
     */
    public static long toExpiresAt(long expiresIn) {
        return ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(expiresIn).toEpochSecond();
    }

    /**
     * Checks if the current time is past the provided expiration time.
     *
     * @param expiresAt The expiration time as an epoch second.
     * @return True if the current time is past the expiration time, false otherwise.
     */
    public static boolean expired(long expiresAt) {
        val currentEpochSeconds = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
        return currentEpochSeconds > expiresAt;
    }
}
