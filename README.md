# MicrosoftAuthenticator

`MicrosoftAuthenticator` is a Java class designed to handle the authentication process with Microsoft and Xbox Live services. It facilitates logging in via a web view, managing token refreshes, and acquiring tokens necessary for Minecraft authentication.

## Overview

This class supports:
- Opening a web view for user login.
- Handling token refresh and expiration.
- Acquiring and validating authentication tokens for Minecraft.

## Features

- **Web Authentication**: Opens a web view for Microsoft account login.
- **Token Management**: Refreshes tokens and handles token expiration.
- **Minecraft Integration**: Validates ownership and acquires Minecraft tokens.

## [Installation](https://jitpack.io/#ClydoNetwork/MicrosoftAuthenticator)

## Dependencies

- **Google Gson**: For JSON serialization and deserialization.
- **JavaFX**: For the web view component (required for web-based authentication).

## Usage

### Setup

To use `MicrosoftAuthenticator`, you'll need to ensure you have the required dependencies in your project, including Gson and JavaFX.

### Example

Here’s an example of how to use the `MicrosoftAuthenticator` class:

```java
import net.clydo.msa.AuthenticatorOptions;
import net.clydo.msa.MicrosoftAuthenticator;
import net.clydo.msa.responses.AuthResult;

import java.util.concurrent.CompletableFuture;

public class AuthExample {
    public static void main(String[] args) {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        
        // Configure the authentication options
        AuthenticatorOptions options = AuthenticatorOptions.builder()
                .title("Microsoft Authentication")
                .width(800)
                .height(600)
                .build();
        
        // Start the authentication process
        CompletableFuture<AuthResult> authFuture = authenticator.asyncWebview(options);

        authFuture.thenAccept(authResult -> {
            System.out.println("Authentication successful!");
            // Handle the authentication result
        }).exceptionally(throwable -> {
            System.err.println("Authentication failed: " + throwable.getMessage());
            return null;
        });
    }
}
```

### Methods

#### `asyncWebview(AuthenticatorOptions authenticatorOptions)`

Starts the authentication process by opening a web view where the user can log in. Returns a `CompletableFuture<AuthResult>` that completes with the authentication result.

**Parameters:**
- `authenticatorOptions`: Configuration for the login window.

**Returns:**
- `CompletableFuture<AuthResult>`: Contains the authentication result.

#### `asyncToken(String refreshToken, String accessToken, long expiresAt, BooleanSupplier forceRefreshToken)`

Handles authentication using a refresh token or an access token. It also manages token expiration and refreshes tokens as needed.

**Parameters:**
- `refreshToken`: The refresh token to obtain a new access token.
- `accessToken`: The current access token.
- `expiresAt`: The expiration time of the current access token (epoch time).
- `forceRefreshToken`: A function to determine if the token should be forcefully refreshed.

**Returns:**
- `CompletableFuture<AuthResult>`: Contains the authentication result.

#### `toExpiresAt(long expiresIn)`

Converts the expiration time from seconds to an epoch time.

**Parameters:**
- `expiresIn`: Number of seconds until expiration.

**Returns:**
- `long`: The expiration time as an epoch second.

#### `expired(long expiresAt)`

Checks if the current time has passed the provided expiration time.

**Parameters:**
- `expiresAt`: The expiration time as an epoch second.

**Returns:**
- `boolean`: `true` if the current time is past the expiration time, otherwise `false`.

## Error Handling

Errors during the authentication process are thrown as `MicrosoftAuthenticatorException`. Ensure to handle these exceptions properly in your code to manage errors such as network issues or authentication failures.

## Notes

- The `asyncWebview` method requires JavaFX to be properly set up in your environment.
- Ensure that the authentication options provided are correctly configured for your application.

## Contributing

If you find any issues or have suggestions for improvements, please feel free to submit an issue or a pull request.
