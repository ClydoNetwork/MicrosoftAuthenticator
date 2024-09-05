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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enumeration representing the different phases of an authentication process.
 * Each phase corresponds to a specific step in the process and is associated with a descriptive message.
 */
@Getter
@RequiredArgsConstructor
public enum Phase {

    /**
     * Phase to display the login page.
     */
    OPEN_LOGIN_PAGE("Display Login Page"),

    /**
     * Phase to acquire a Microsoft account during the authentication process.
     */
    ACQUIRE_MICROSOFT_ACCOUNT("Acquire Microsoft Account"),

    /**
     * Phase to refresh the Microsoft account credentials if needed.
     */
    REFRESH_MICROSOFT_ACCOUNT("Refresh Microsoft Account"),

    /**
     * Phase to acquire an XBL (Xbox Live) token.
     */
    ACQUIRE_XBL_TOKEN("Acquire XBL Account"),

    /**
     * Phase to acquire an XSTS (Xbox Security Token Service) token.
     */
    ACQUIRE_XSTS_TOKEN("Acquire XSTS Account"),

    /**
     * Phase to acquire a Minecraft account.
     */
    ACQUIRE_MINECRAFT_ACCOUNT("Acquire Minecraft Account"),

    /**
     * Phase to acquire information related to the Minecraft store for the authenticated user.
     */
    ACQUIRE_MINECRAFT_STORE("Acquire Minecraft Store"),

    /**
     * Phase to acquire the Minecraft profile associated with the authenticated user.
     */
    ACQUIRE_MINECRAFT_PROFILE("Acquire Minecraft Profile");

    /**
     * A descriptive message associated with the phase.
     */
    private final String message;
}
