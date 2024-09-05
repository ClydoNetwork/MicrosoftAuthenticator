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

import lombok.Builder;

import java.awt.*;
import java.util.List;

/**
 * A configuration class that encapsulates options for an authenticator.
 * This class is immutable and can be built using the {@link Builder}.
 */
@Builder(builderClassName = "Builder")
public record AuthenticatorOptions(
        /*
          The title of the authenticator window.
         */
        String title,

        /*
         * The width of the authenticator window in pixels.
         */
        int width,

        /*
         * The height of the authenticator window in pixels.
         */
        int height,

        /*
         * A list of images to be used as icons for the authenticator window.
         */
        List<Image> icons,

        /*
         * Whether the authenticator window should always stay on top of other windows.
         */
        boolean alwaysOnTop,

        /*
         * Whether the authenticator window is resizable.
         */
        boolean resizeable,

        /*
         * Whether the context menu is enabled in the authenticator window.
         */
        boolean contextMenu,

        /*
         * Whether JavaScript is enabled in the authenticator window.
         */
        boolean javaScript,

        /*
         * The user agent string to be used for the authenticator window.
         */
        String userAgent
) {
}