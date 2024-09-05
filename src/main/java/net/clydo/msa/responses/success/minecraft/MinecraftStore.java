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
 * Represents a store in Minecraft, containing information about available products and associated metadata.
 *
 * <p>This record includes an array of store products, a signature for the store data, and a key ID for validation.</p>
 */
public record MinecraftStore(
        /*
         * An array of {@link StoreProduct} objects representing the products available in the Minecraft store.
         */
        @SerializedName(value = "items") StoreProduct[] items,

        /*
         * A signature used to verify the authenticity of the store data.
         */
        @SerializedName(value = "signature") String signature,

        /*
         * A unique key ID used for identifying or validating the store data.
         */
        @SerializedName(value = "keyId") String keyId
) {
    /**
     * Represents a product available in the Minecraft store.
     *
     * <p>This record includes details about the product's name and a signature for validation.</p>
     */
    public record StoreProduct(
            /*
             * The name of the store product.
             */
            @SerializedName(value = "name") String name,

            /*
             * A signature used to verify the authenticity of the product data.
             */
            @SerializedName(value = "signature") String signature
    ) {
    }
}