package ch.fhnw.team6;

import ch.fhnw.team6.view.AnimationManager;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.jwm.skija.LayerGLSkija;
import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

public class Main {

    private Window window; // The main application window
    private AnimationManager animationManager; // Manages the animations
    private long lastFrameTime = System.nanoTime(); // Time of the last frame in nanoseconds

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        App.start(() -> {
            window = App.makeWindow();
            window.setLayer(new LayerGLSkija());
            window.setTitle("Animation Test");

            // Linux-specific fullscreen setup
            if (Platform.CURRENT == Platform.X11) {
                // Proper Linux fullscreen approach
                Screen screen = App.getPrimaryScreen();
                window.setWindowPosition(0, 0);
                window.setWindowSize(screen.getWorkArea().getWidth(), screen.getWorkArea().getHeight());
                window.setVisible(true);
                window.setFullScreen(true);
            } else {
                // Standard approach for other platforms
                window.setFullScreen(true);
                window.setVisible(true);
            }

            // Initialize after window is ready
            animationManager = new AnimationManager(window);

            window.setEventListener(this::handleEvent);
            window.requestFrame();
        });
    }

    private void handleEvent(Event e) {
        switch (e) {
            case EventWindowCloseRequest _ -> App.terminate();
            case EventKey keyEvent -> {
                if (keyEvent.isPressed()) {
                    switch (keyEvent.getKey()) {
                        case SPACE -> animationManager.nextAnimation();
                        case Q, ESCAPE -> App.terminate();
                        case F11 -> toggleFullscreen(); // Add fullscreen toggle
                        default -> System.out.println("Key pressed: " + keyEvent.getKey());
                    }
                }
            }
            case EventFrameSkija frameEvent -> handleFrameEvent(frameEvent);
            default -> {
            }
        }
    }

    private void toggleFullscreen() {
        if (window.isFullScreen()) {
            window.setFullScreen(false);
            window.setWindowSize(1024, 768); // Default windowed size
            Screen screen = App.getPrimaryScreen();
            int centerX = (screen.getWorkArea().getWidth() - window.getWindowRect().getWidth()) / 2;
            int centerY = (screen.getWorkArea().getHeight() - window.getWindowRect().getHeight()) / 2;
            window.setWindowPosition(centerX, centerY);
        } else {
            if (Platform.CURRENT == Platform.X11) {
                Screen screen = App.getPrimaryScreen();
                window.setWindowPosition(0, 0);
                window.setWindowSize(screen.getWorkArea().getWidth(), screen.getWorkArea().getHeight());
            }
            window.setFullScreen(true);
        }
    }

    private void handleFrameEvent(EventFrameSkija frameEvent) {
        Canvas canvas = frameEvent.getSurface().getCanvas();

        // Black background covering potential gaps
        try (Paint bgPaint = new Paint()) {
            bgPaint.setColor(0xFF000000);
            IRect bounds = window.getWindowRect();
            canvas.drawRect(Rect.makeXYWH(0, 0, bounds.getWidth(), bounds.getHeight()), bgPaint);
        }

        // Calculate delta time
        long now = System.nanoTime();
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        // Update and draw animations
        animationManager.update(deltaTime);
        animationManager.draw(canvas);

        window.requestFrame();
    }
}