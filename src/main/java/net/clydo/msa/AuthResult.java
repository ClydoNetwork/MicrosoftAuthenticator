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

import net.clydo.msa.responses.success.microsoft.MicrosoftAccount;
import net.clydo.msa.responses.success.microsoft.XBLToken;
import net.clydo.msa.responses.success.microsoft.XSTSToken;
import net.clydo.msa.responses.success.minecraft.MinecraftAccount;
import net.clydo.msa.responses.success.minecraft.MinecraftProfile;
import net.clydo.msa.responses.success.minecraft.MinecraftStore;

/**
 * A record that encapsulates the result of an authentication process.
 * This class is immutable and can be built using the {@link AuthResult.AuthResultBuilder}.
 */
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
    public static AuthResultBuilder builder() {
        return new AuthResultBuilder();
    }

    public static class AuthResultBuilder {
        private MicrosoftAccount microsoftAccount;
        private XBLToken xblToken;
        private XSTSToken xstsToken;
        private MinecraftAccount minecraftAccount;
        private MinecraftStore minecraftStore;
        private MinecraftProfile minecraftProfile;

        AuthResultBuilder() {
        }

        public AuthResultBuilder microsoftAccount(MicrosoftAccount microsoftAccount) {
            this.microsoftAccount = microsoftAccount;
            return this;
        }

        public AuthResultBuilder xblToken(XBLToken xblToken) {
            this.xblToken = xblToken;
            return this;
        }

        public AuthResultBuilder xstsToken(XSTSToken xstsToken) {
            this.xstsToken = xstsToken;
            return this;
        }

        public AuthResultBuilder minecraftAccount(MinecraftAccount minecraftAccount) {
            this.minecraftAccount = minecraftAccount;
            return this;
        }

        public AuthResultBuilder minecraftStore(MinecraftStore minecraftStore) {
            this.minecraftStore = minecraftStore;
            return this;
        }

        public AuthResultBuilder minecraftProfile(MinecraftProfile minecraftProfile) {
            this.minecraftProfile = minecraftProfile;
            return this;
        }

        public AuthResult build() {
            return new AuthResult(this.microsoftAccount, this.xblToken, this.xstsToken, this.minecraftAccount, this.minecraftStore, this.minecraftProfile);
        }

        public String toString() {
            return "AuthResult.AuthResultBuilder(microsoftAccount=" + this.microsoftAccount + ", xblToken=" + this.xblToken + ", xstsToken=" + this.xstsToken + ", minecraftAccount=" + this.minecraftAccount + ", minecraftStore=" + this.minecraftStore + ", minecraftProfile=" + this.minecraftProfile + ")";
        }
    }
}
