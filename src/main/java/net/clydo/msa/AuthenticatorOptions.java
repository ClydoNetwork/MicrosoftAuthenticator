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