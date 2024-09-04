package net.clydo.msa;

import lombok.Builder;
import net.clydo.msa.responses.success.microsoft.MicrosoftAccount;
import net.clydo.msa.responses.success.microsoft.XBLToken;
import net.clydo.msa.responses.success.microsoft.XSTSToken;
import net.clydo.msa.responses.success.minecraft.MinecraftAccount;
import net.clydo.msa.responses.success.minecraft.MinecraftProfile;
import net.clydo.msa.responses.success.minecraft.MinecraftStore;

/**
 * A record that encapsulates the result of an authentication process.
 * This class is immutable and can be built using the {@link AuthResult.Builder}.
 */
@Builder(builderClassName = "Builder")
public record AuthResult(

        /*
         * The Microsoft account associated with the authentication.
         */
        MicrosoftAccount microsoftAccount,

        /*
         * The XBL (Xbox Live) token received during authentication.
         */
        XBLToken xblToken,

        /*
         * The XSTS (Xbox Security Token Service) token received during authentication.
         */
        XSTSToken xstsToken,

        /*
         * The Minecraft account associated with the authentication.
         */
        MinecraftAccount minecraftAccount,

        /*
         * The Minecraft store information related to the authenticated user.
         */
        MinecraftStore minecraftStore,

        /*
         * The Minecraft profile information associated with the authenticated user.
         */
        MinecraftProfile minecraftProfile
) {
}
