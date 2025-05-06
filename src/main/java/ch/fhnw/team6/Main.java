package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.view.AnimationManager;
import ch.fhnw.team6.view.QuestionPane;
import ch.fhnw.team6.view.TextAlign;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private Canvas canvas;

    private AnimationManager animationManager;
    private InputHandler inputHandler;

    private QuestionPane questionPane;
    private Player player;
    
    private String currentQuestion;
    private String currentAnswer;
    private boolean isAnswered;

    private int step;
    private long lastFrameTime;
    private String input = "";

    /**
     * Initializes the JavaFX application
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        initializeGame(primaryStage);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, 1280, 720, Color.BLACK);
        primaryStage.setScene(scene);

        setupStage(primaryStage);

        // Key event handling for user input
        scene.setOnKeyPressed(e -> handleKeyEvent(e.getCode()));

        // Start the animation timer
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGameLoop(now);
            }
        }.start();
    }

    /**
     * Initializes the game components
     * 
     * Creates the canvas, input handler, and animation manager
     * Sets the initial question and answer state
     * 
     * @param primaryStage The primary stage for this application
     */
    private void initializeGame(Stage primaryStage) {
        primaryStage.setTitle("Animation Test");

        // Initialize components
        player = new Player();
        canvas = new Canvas(1280, 720);
        inputHandler = new InputHandler(player);
        animationManager = new AnimationManager(canvas, 7, 4);

        // Initial question and answer state
        currentQuestion = inputHandler.getQuestionQuestion();
        currentAnswer = "";
        isAnswered = false;
        step = 0;
        lastFrameTime = System.nanoTime();
    }

    /**
     * Sets up the primary stage for the application.
     * 
     * @param primaryStage The primary stage for this application.
     */
    private void setupStage(Stage primaryStage) {
        primaryStage.setFullScreen(true); // Enable fullscreen mode
        primaryStage.show();

        // Adjust canvas size on window resize or fullscreen toggle
        primaryStage.widthProperty().addListener((_, _, _) -> updateCanvasSize(primaryStage));
        primaryStage.heightProperty().addListener((_, _, _) -> updateCanvasSize(primaryStage));
    }

    /**
     * Updates the game loop
     * 
     * Calculates the delta time since the last frame
     * Updates the animation manager
     * Draws the current animation and question/answer text
     * 
     * @param now The current time in nanoseconds
     */
    private void updateGameLoop(long now) {
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);
        animationManager.update(deltaTime);
        animationManager.draw(gc);
        updateTextPane();
    }

    /**
     * Clears the canvas by filling it with a black rectangle
     * 
     * @param gc The GraphicsContext to clear
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Updates the text pane with the current question and answer
     * 
     * Sets the text alignment, background opacity, and position
     * Draws the question and answer on the canvas
     */
    private void updateTextPane() {
        if (questionPane == null) {
            questionPane = new QuestionPane(0, (float) canvas.getHeight(), (float) canvas.getWidth() * 0.8f, (float) canvas.getHeight() / 6f);
            questionPane.setTextAlign(TextAlign.CENTER);
            questionPane.setBackgroundOpacity(0.75);
            questionPane.setPosition(((float) canvas.getWidth()-questionPane.getWidth()) * 0.5f, (float) (canvas.getHeight() - questionPane.getHeight()) / 2);
            questionPane.setCornerRadius(90);
        }
        questionPane.setQuestion(currentQuestion);
        questionPane.setAnswer(currentAnswer);
        questionPane.draw(canvas);
    }

    /**
     * Handles key events for user input
     * 
     * Processes key presses for language switch, space key, escape key, and answer input
     * 
     * @param code The key code of the pressed key
     */
    private void handleKeyEvent(KeyCode code) {
        switch (code) {
            case L -> handleLanguageSwitch();
            case SPACE -> handleSpaceKey();
            case ESCAPE -> System.exit(0);
            case R -> restartGame();
            default -> handleAnswerInput(code);
        }
    }

    /**
     * Handles the language switch event
     * 
     * Switches the player's language to the next available language
     */
    private void handleLanguageSwitch() {
        player.setLanguage(Language.getNextLanguage(player.getLanguage()));
        updateLanguage();
    }

    /**
     * Updates the current question based on the player's language
     * 
     * Retrieves the question from the input handler based on the current language
     */
    private void updateLanguage() {
        currentQuestion = inputHandler.getQuestionQuestion();
        if(isAnswered) {
            try {
                currentAnswer = inputHandler.answerQuestion(input);
            } catch (NotAValidInputException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the space key event
     * 
     * If the question is answered, it proceeds to the next animation
     * If the step is 7, it restarts the game
     */
    private void handleSpaceKey() {
        if (isAnswered) {
            animationManager.nextAnimation();
            currentAnswer = "";
            inputHandler.nextQuestion();
            currentQuestion = inputHandler.getQuestionQuestion();
            isAnswered = false;
            if (step == 7) {
                restartGame();
            }
        }
    }

    /**
     * Handles the answer input from the user
     * 
     * @param code The key code of the pressed key
     */
    private void handleAnswerInput(KeyCode code) {
        if (!isAnswered) {
            try {
                input = code.getName();
                currentAnswer = inputHandler.answerQuestion(input);
                isAnswered = true;
                step++;
            } catch (NotAValidInputException e) {
                System.err.println("Invalid input: " + e.getMessage());
            }
        }
    }

    /**
     * Updates the canvas size based on the primary stage dimensions
     * 
     * Sets the canvas width and height to match the primary stage
     * Updates the question pane size and position
     * 
     * @param primaryStage The primary stage for this application
     */
    private void updateCanvasSize(Stage primaryStage) {
        canvas.setWidth(primaryStage.getWidth());
        canvas.setHeight(primaryStage.getHeight());

        // Update the text pane size and position
        if (questionPane != null) {
            questionPane.setSize((float) primaryStage.getWidth() * 0.8f, (float) primaryStage.getHeight() / 6);
            questionPane.setPosition(((float) primaryStage.getWidth()-questionPane.getWidth()) * 0.5f, (float) (primaryStage.getHeight() - questionPane.getHeight()) / 2);
            questionPane.setFontSize(28);
            questionPane.setLineSpacing(12);
            questionPane.setPaddingY(20);
        }
    }

    /**
     * Restarts the game by resetting the input handler and question state
     * 
     * Resets the current question, answer, and step
     */
    private void restartGame() {
        player = new Player();
        inputHandler.restartGame(player);
        currentQuestion = inputHandler.getQuestionQuestion();
        currentAnswer = "";
        isAnswered = false;
        step = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
