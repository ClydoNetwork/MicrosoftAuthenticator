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
 * Represents a Minecraft player profile, including player details and associated skins.
 *
 * <p>This record contains the player's ID, name, and an array of their skins.</p>
 */
public record MinecraftProfile(
        /**
         * The unique identifier of the Minecraft profile.
         */
        @SerializedName(value = "id") String id,

        /**
         * The name associated with the Minecraft profile.
         */
        @SerializedName(value = "name") String name,

        /**
         * An array of {@link MinecraftSkin} objects representing the skins associated with the Minecraft profile.
         */
        @SerializedName(value = "skins") MinecraftSkin[] skins
) {
    /**
     * Represents a skin associated with a Minecraft profile.
     *
     * <p>This record contains details about the skin, including its ID, state, URL, variant, alias, and texture key.</p>
     */
    public record MinecraftSkin(
            /*
             * The unique identifier of the skin.
             */
            @SerializedName(value = "id") String id,

            /*
             * The state of the skin (e.g., active, inactive).
             */
            @SerializedName(value = "state") String state,

            /*
             * The URL where the skin texture can be accessed.
             */
            @SerializedName(value = "url") String url,

            /*
             * The variant of the skin (e.g., slim, classic).
             */
            @SerializedName(value = "variant") String variant,

            /*
             * An alias for the skin, if applicable.
             */
            @SerializedName(value = "alias") String alias,

            /*
             * A key used to identify the texture associated with the skin.
             */
            @SerializedName(value = "textureKey") String textureKey
    ) {
    }
}