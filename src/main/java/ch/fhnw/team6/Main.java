package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.view.AnimationManager;
import ch.fhnw.team6.view.TextPane;
import ch.fhnw.team6.model.Player;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.jwm.skija.LayerGLSkija;
import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

public class Main {

    private Window window; // The main application window
    private AnimationManager animationManager; // Manages the animations
    private long lastFrameTime = System.nanoTime(); // Time of the last frame in nanoseconds
    private TextPane questionPane; // Pane for displaying questions
    private InputHandler inputHandler = new InputHandler(new Player()); // Handles user input
    private String currentQuestion = inputHandler.getQuestionQuestion(); // Current question text
    private String currentAnswer = ""; // Current answer text
    private boolean isAnswered = false; // Flag to check if the question has been answered
    private int step = 0; // Step counter for the game

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
            case EventKey keyEvent -> handleKeyEvent(keyEvent);
            case EventFrameSkija frameEvent -> handleFrameEvent(frameEvent);
            default -> {
            }
        }
    }

    private void handleKeyEvent(EventKey keyEvent) {
        if (keyEvent.isPressed()) {
            switch (keyEvent.getKey()) {
                case SPACE -> {
                    if (isAnswered) {
                        animationManager.nextAnimation();
                        currentAnswer = "";
                        currentQuestion = inputHandler.getQuestionQuestion();
                        isAnswered = false;
                        if (step == 7) {
                            restartGame();
                        }
                    }
                }
                case ESCAPE -> App.terminate();
                case R -> {
                    restartGame();
                }
                default -> {
                    try  {
                        currentAnswer = inputHandler.answerQuestion(keyEvent.getKey().getName()); 
                        isAnswered = true;           
                        step++;            
                    } catch (NotAValidInputException e){
                        System.err.println("Invalid input: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void restartGame() {
        // Restart the game logic
        inputHandler.restartGame(new Player());
        currentQuestion = inputHandler.getQuestionQuestion();
        currentAnswer = "";
        isAnswered = false;
        step = 0;
    }

    private void handleFrameEvent(EventFrameSkija frameEvent) {
        Canvas canvas = frameEvent.getSurface().getCanvas();

        IRect bounds = window.getWindowRect();
        // Black background covering potential gaps
        try (Paint bgPaint = new Paint()) {
            bgPaint.setColor(0xFF000000);
            canvas.drawRect(Rect.makeXYWH(0, 0, bounds.getWidth(), bounds.getHeight()), bgPaint);
        }

        float width = bounds.getWidth();
        float height = bounds.getHeight() / 6;
        float paneY = (bounds.getHeight() - height) / 2; // Center vertically

        questionPane = new TextPane(0, paneY, width, height);
        questionPane.setQuestion(currentQuestion);
        questionPane.setAnswer(currentAnswer);

        // Calculate delta time
        long now = System.nanoTime();
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        // Update and draw animations
        animationManager.update(deltaTime);
        animationManager.draw(canvas);
        questionPane.draw(canvas);

        window.requestFrame();
    }
}