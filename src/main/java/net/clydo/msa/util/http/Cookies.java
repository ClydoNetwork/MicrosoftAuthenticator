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