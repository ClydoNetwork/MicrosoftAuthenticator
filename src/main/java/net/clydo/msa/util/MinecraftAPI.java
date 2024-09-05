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
import net.clydo.msa.responses.error.MinecraftError;
import net.clydo.msa.responses.success.minecraft.MinecraftAccount;
import net.clydo.msa.responses.success.minecraft.MinecraftProfile;
import net.clydo.msa.responses.success.minecraft.MinecraftStore;
import net.clydo.msa.util.http.HttpUtil;
import net.clydo.msa.util.http.MimeType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Utility class for interacting with Minecraft's various API endpoints.
 * Provides methods to acquire the Minecraft authentication token, store entitlements, and user profile.
 */
@UtilityClass
public class MinecraftAPI {

    // Minecraft API endpoints for authentication, store entitlements, and profile information
    public final String MINECRAFT_AUTH_ENDPOINT = "https://api.minecraftservices.com/authentication/login_with_xbox";
    public final String MINECRAFT_STORE_ENDPOINT = "https://api.minecraftservices.com/entitlements/mcstore";
    public final String MINECRAFT_PROFILE_ENDPOINT = "https://api.minecraftservices.com/minecraft/profile";

    /**
     * Acquires a Minecraft authentication token using the provided Microsoft authenticator, user hash, and XSTS token.
     *
     * @param authenticator   The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder   The builder for constructing the authentication result.
     * @param userHash        The user hash obtained from the XBL authentication.
     * @param xstsToken       The XSTS token obtained from the Microsoft authentication.
     * @return A {@link MinecraftAccount} representing the acquired Minecraft account information.
     * @throws MicrosoftAuthenticatorException if the authentication fails.
     */
    public MinecraftAccount acquireMinecraftToken(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String userHash,
            String xstsToken
    ) {
        // Set the phase to acquiring Minecraft account
        authenticator.setPhase(Phase.ACQUIRE_MINECRAFT_ACCOUNT);

        // Send a POST request to the Minecraft authentication endpoint
        val minecraftAccount = HttpUtil.POST(
                MinecraftAPI.MINECRAFT_AUTH_ENDPOINT,
                MimeType.APPLICATION_JSON,
                MimeType.APPLICATION_JSON,
                Map.of(
                        "identityToken", String.format("XBL3.0 x=%s;%s", userHash, xstsToken)
                ),
                MinecraftAccount.class,
                MinecraftError.class
        );

        // Set the acquired Minecraft account in the result builder
        resultBuilder.minecraftAccount(minecraftAccount);

        return minecraftAccount;
    }

    /**
     * Acquires the Minecraft store entitlements for the authenticated user.
     *
     * @param authenticator   The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder   The builder for constructing the authentication result.
     * @param mcAccessToken   The Minecraft access token.
     * @return A {@link MinecraftStore} representing the user's store entitlements.
     * @throws MicrosoftAuthenticatorException if the request fails.
     */
    public MinecraftStore acquireMinecraftStore(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String mcAccessToken
    ) {
        // Set the phase to acquiring Minecraft store entitlements
        authenticator.setPhase(Phase.ACQUIRE_MINECRAFT_STORE);

        // Send a GET request to the Minecraft store endpoint
        val minecraftStore = HttpUtil.GET(
                MinecraftAPI.MINECRAFT_STORE_ENDPOINT,
                mcAccessToken,
                MinecraftStore.class,
                MinecraftError.class
        );

        // Set the acquired Minecraft store in the result builder
        resultBuilder.minecraftStore(minecraftStore);

        return minecraftStore;
    }

    /**
     * Acquires the Minecraft profile information for the authenticated user.
     *
     * @param authenticator   The MicrosoftAuthenticator instance to manage the authentication flow.
     * @param resultBuilder   The builder for constructing the authentication result.
     * @param mcAccessToken   The Minecraft access token.
     * @return A {@link MinecraftProfile} representing the user's profile information.
     * @throws MicrosoftAuthenticatorException if the request fails.
     */
    public MinecraftProfile acquireMinecraftProfile(
            @NotNull MicrosoftAuthenticator authenticator,
            @NotNull AuthResult.AuthResultBuilder resultBuilder,
            String mcAccessToken
    ) {
        // Set the phase to acquiring Minecraft profile information
        authenticator.setPhase(Phase.ACQUIRE_MINECRAFT_PROFILE);

        // Send a GET request to the Minecraft profile endpoint
        val minecraftProfile = HttpUtil.GET(
                MinecraftAPI.MINECRAFT_PROFILE_ENDPOINT,
                mcAccessToken,
                MinecraftProfile.class,
                MinecraftError.class
        );

        // Set the acquired Minecraft profile in the result builder
        resultBuilder.minecraftProfile(minecraftProfile);

        return minecraftProfile;
    }
}