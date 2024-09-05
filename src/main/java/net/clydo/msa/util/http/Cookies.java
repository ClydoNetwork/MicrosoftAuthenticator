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

package net.clydo.msa.util.http;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * A utility class for managing cookies.
 * This class provides methods to get or create a {@link CookieManager} instance and manage cookies.
 */
@UtilityClass
public class Cookies {

    /**
     * Retrieves the current {@link CookieManager} if one is already set, or creates a new one if not.
     *
     * <p>If a {@link CookieManager} is already set as the default {@link CookieHandler}, it is returned
     * and its cookie store is cleared. Otherwise, a new {@link CookieManager} is created, set as the default,
     * and returned with an empty cookie store.</p>
     *
     * @return the existing or newly created {@link CookieManager} instance with an empty cookie store.
     */
    public CookieManager getOrCreate() {
        CookieManager cookieManager;

        val previous = CookieHandler.getDefault();
        if (previous instanceof CookieManager) {
            cookieManager = (CookieManager) previous;
        } else {
            CookieHandler.setDefault(cookieManager = new CookieManager());
        }

        cookieManager.getCookieStore().removeAll();
        return cookieManager;
    }
}