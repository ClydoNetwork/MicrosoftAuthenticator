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