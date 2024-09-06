import net.clydo.msa.AuthResult;
import net.clydo.msa.MicrosoftAuthenticator;

import java.util.concurrent.CompletableFuture;

public class TestLogin {
    public static void main(String[] args) {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();

        // Start the authentication process
        CompletableFuture<AuthResult> authFuture = authenticator.asyncExternal();

        authFuture.thenAccept(authResult -> {
            System.out.println("Authentication successful!");
            // Handle the authentication result
        }).exceptionally(throwable -> {
            System.err.println("Authentication failed: " + throwable.getMessage());
            return null;
        });
        authFuture.join();
    }
}
