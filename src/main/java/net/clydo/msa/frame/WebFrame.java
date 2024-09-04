package net.clydo.msa.frame;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.val;
import net.clydo.msa.AuthenticatorOptions;
import net.clydo.msa.responses.error.MicrosoftAuthenticatorException;
import net.clydo.msa.responses.error.URLError;
import net.clydo.msa.util.URLUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

/**
 * A Swing-based frame that displays a web view for handling authentication flows.
 *
 * <p>This class provides a graphical interface for web-based authentication using JavaFX's WebView component.
 * It integrates with Swing to manage authentication processes and handle errors.</p>
 */
public class WebFrame extends JFrame implements Closeable {
    private final AuthenticatorOptions authenticatorOptions;
    private CompletableFuture<String> future;
    @Getter
    private WebView webView;
    private boolean first;
    private ProgressIndicator loadingIndicator;
    private JFXPanel contentPane;
    private boolean close;

    /**
     * Constructs a new {@code WebFrame} with the specified authentication options.
     *
     * @param authenticatorOptions the configuration options for the authentication frame.
     */
    public WebFrame(@NotNull AuthenticatorOptions authenticatorOptions) {
        this.authenticatorOptions = authenticatorOptions;
        this.initializeFrame();
    }

    /**
     * Initializes the frame with settings from {@link AuthenticatorOptions}.
     */
    private void initializeFrame() {
        val frameOptions = this.authenticatorOptions;
        this.setTitle(frameOptions.title());
        this.setSize(frameOptions.width(), frameOptions.height());
        this.setIconImages(frameOptions.icons());
        this.setAlwaysOnTop(frameOptions.alwaysOnTop());
        this.setResizable(frameOptions.resizeable());
        this.setLocationRelativeTo(null);
        this.setContentPane(this.contentPane = new JFXPanel());

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * Starts the authentication process by loading the specified URL.
     *
     * @param url the URL to load in the web view for authentication.
     * @return a {@link CompletableFuture} that will be completed with the authentication code or exceptionally with an error.
     */
    public CompletableFuture<String> start(String url) {
        this.first = true;

        if (this.future != null && !this.future.isDone()) {
            closeWindowWithError("New window opened");
        }

        this.future = new CompletableFuture<>();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindowWithError("User closed the authentication window");
            }
        });

        Platform.runLater(() -> this.initializeWebView(url));
        return this.future;
    }

    /**
     * Initializes and displays the JavaFX WebView with the given URL.
     *
     * @param url the URL to load in the WebView.
     */
    private void initializeWebView(String url) {
        try {
            this.setVisible(true);

            this.webView = new WebView();
            this.loadingIndicator = new ProgressIndicator(0);
            this.loadingIndicator.setScaleX(2);
            this.loadingIndicator.setScaleY(2);
            this.loadingIndicator.setStyle("""
                                    -fx-accent: rgb(255, 125, 0);
                                    -fx-control-inner-background: white;
                                    -fx-background-color: transparent;
                    """);
            this.loadingIndicator.setVisible(true);

            val engine = webView.getEngine();
            engine.load(url);

            val root = new BorderPane();
            root.setCenter(this.loadingIndicator);

            val loadWorker = engine.getLoadWorker();
            loadWorker.stateProperty().addListener((observable, oldValue, newValue) -> this.handleLoadStateChange(engine, newValue));
            loadWorker.progressProperty().addListener((observable, oldValue, newValue) -> this.handleLoadProgressChange(newValue.doubleValue()));
            engine.locationProperty().addListener((observable, oldValue, newValue) -> this.handleLocationChange(newValue));

            this.configureWebView(this.webView);

            val content = this.getContentPane();
            content.setScene(new Scene(root, this.getWidth(), this.getHeight()));
        } catch (Throwable t) {
            if (!this.close) {
                throw t;
            }
        }
    }

    /**
     * Configures the WebView component with settings from {@link AuthenticatorOptions}.
     *
     * @param webView the WebView to configure.
     */
    private void configureWebView(@NotNull WebView webView) {
        try {
            webView.getEngine().setUserDataDirectory(null);
            webView.setCache(false);
            webView.setFontSmoothingType(FontSmoothingType.GRAY);
            webView.setContextMenuEnabled(this.authenticatorOptions.contextMenu());
            webView.getEngine().setJavaScriptEnabled(this.authenticatorOptions.javaScript());
            webView.getEngine().setUserAgent(this.authenticatorOptions.userAgent());
            webView.setPrefSize(this.authenticatorOptions.width(), this.authenticatorOptions.height());
        } catch (Throwable t) {
            if (!this.close) {
                throw t;
            }
        }
    }

    /**
     * Handles changes in the loading progress of the WebView.
     *
     * @param newProgress the new progress value (0.0 to 1.0).
     */
    private void handleLoadProgressChange(double newProgress) {
        if (this.close) {
            return;
        }
        if (this.loadingIndicator == null) {
            return;
        }
        this.loadingIndicator.setProgress(newProgress);
    }

    /**
     * Handles changes in the load state of the WebView.
     *
     * @param engine the WebEngine associated with the WebView.
     * @param state  the new state of the WebEngine's load worker.
     */
    private void handleLoadStateChange(WebEngine engine, @NotNull Worker.State state) {
        if (this.close) {
            return;
        }

        try {
            switch (state) {
                case CANCELLED -> this.closeWindowWithError("Authentication window: " + state.name());
                case FAILED -> this.closeWindowWithError("Authentication window failed to load.");
                case SUCCEEDED -> {
                    val script = this.getCssStyleScript();
                    engine.executeScript(script);
                    val js = "document.querySelectorAll('img').forEach(img => img.ondragstart = () => false);";
                    engine.executeScript(js);

                    if (this.first) {
                        this.first = false;

                        val scene = this.contentPane.getScene();
                        if (scene.getRoot() instanceof BorderPane borderPane) {
                            borderPane.setCenter(this.webView);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            if (!this.close) {
                throw t;
            }
        }
    }

    /**
     * Generates a CSS style script to apply custom styles to the WebView content.
     *
     * @return the CSS style script as a string.
     */
    private @NotNull String getCssStyleScript() {
        var css =
                "body { " +
                        "   user-select: none; " +
                        "   -webkit-user-select: none; " +
                        "   cursor: default; " +
                        "   -webkit-font-smoothing: antialiased; " +
                        "   -moz-osx-font-smoothing: grayscale; " +
                        "   text-rendering: optimizeLegibility; " +
                        "   font-feature-settings: 'liga' 1; " +
                        "} " +
                        "p, span, div, textarea { " +
                        "   cursor: default; " +
                        "} " +
                        "img { " +
                        "   user-drag: none; " +
                        "   -webkit-user-drag: none; " +
                        "   -webkit-transform: translateZ(0); " +
                        "   image-rendering: auto; " +
                        "   max-width: 100%; " +
                        "   max-height: 100%; " +
                        "}";

        css = css.replace("'", "\\'").replace("\"", "\\\"");

        return "var style = document.createElement('style'); style.innerHTML = \"" + css + "\"; document.head.appendChild(style);";
    }

    /**
     * Handles changes in the current location (URL) of the WebView.
     *
     * @param newLocation the new URL location of the WebView.
     */
    private void handleLocationChange(String newLocation) {
        if (this.close) {
            return;
        }

        val code = URLUtil.getQueryParam(newLocation, "code");
        val error = URLUtil.getQueryParam(newLocation, "error");
        val errorDescription = URLUtil.getQueryParam(newLocation, "error_description");

        if (error != null) {
            this.handleAuthError(error, errorDescription);
        } else if (code != null) {
            this.handleAuthSuccess(code);
        }
    }

    /**
     * Handles authentication errors by completing the future exceptionally and closing the window.
     *
     * @param error            the error code.
     * @param errorDescription the error description.
     */
    private void handleAuthError(String error, String errorDescription) {
        if (this.close) {
            return;
        }
        this.closeWindow();
        if (errorDescription != null) {
            this.future.completeExceptionally(new MicrosoftAuthenticatorException(new URLError(error, errorDescription)));
        } else {
            this.future.complete(null);
        }
    }

    /**
     * Handles successful authentication by completing the future with the authorization code and closing the window.
     *
     * @param code the authorization code.
     */
    private void handleAuthSuccess(String code) {
        if (this.close) {
            return;
        }
        this.closeWindow();
        this.future.complete(code);
    }

    /**
     * Closes the authentication window with an error message.
     *
     * @param message the error message.
     */
    private void closeWindowWithError(String message) {
        this.closeWindow();
        this.future.completeExceptionally(new MicrosoftAuthenticatorException(message));
    }

    /**
     * Closes the authentication window and performs cleanup.
     */
    private void closeWindow() {
        this.setVisible(false);

        SwingUtilities.invokeLater(() -> {
            this.setVisible(false);  // Hide the window
            //this.dispose();          // Dispose of resources
            Platform.exit();         // Ensure JavaFX resources are also cleaned up
            if (this.webView != null) {
                Platform.runLater(() -> {
                    if (this.webView != null) {
                        this.webView.getEngine().getLoadWorker().cancel(); // Cancel any ongoing work
                        this.webView.getEngine().load(null);  // Stop loading
                        this.webView = null;  // Release the WebView
                    }
                });
            }
        });
    }

    @Override
    public JFXPanel getContentPane() {
        return (JFXPanel) super.getContentPane();
    }

    @Override
    public void close() {
        this.closeWindow();
        this.close = true;
        this.webView = null;
        this.future = null;
        this.loadingIndicator = null;
        this.contentPane = null;
    }
}