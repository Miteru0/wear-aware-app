package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.view.*;
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

    // ─── Constants ─────────────────────────────────────────────────────
    private static final int TOTAL_STEPS = 7;
    private static final double WINDOWED_WIDTH = 1280;
    private static final double WINDOWED_HEIGHT = 720;

    // ─── GUI Components ─────────────────────────────────────────────────
    private Canvas canvas;
    private BackgroundManager backgroundAnimation;
    private FlagsManager flagsManager;
    private QuestionPane questionPane;

    // ─── Game Logic ─────────────────────────────────────────────────────
    private InputHandler inputHandler;
    private Player player;

    // ─── Game State ─────────────────────────────────────────────────────
    private String currentQuestion = "";
    private String currentAnswer = "";
    private boolean isAnswered = false;
    private boolean isGameStarted = false;
    private boolean isGameEnded = false;
    private int step = 0;
    private long lastFrameTime;
    private String input = "";

    // ─── Application Entry Point ────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeGame(primaryStage);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, WINDOWED_WIDTH, WINDOWED_HEIGHT, Color.BLACK);
        primaryStage.setScene(scene);
        setupStage(primaryStage);
        setupKeyControls(scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGameLoop(now);
            }
        }.start();
    }

    // ─── Initialization ────────────────────────────────────────────────
    private void initializeGame(Stage primaryStage) {
        primaryStage.setTitle("Animation Test");

        player = new Player();
        canvas = new Canvas(WINDOWED_WIDTH, WINDOWED_HEIGHT);
        inputHandler = new InputHandler(player);
        backgroundAnimation = new BackgroundManager(canvas, TOTAL_STEPS, 10, 1280, 720);

        flagsManager = new FlagsManager(canvas.getWidth() - 4 * 75, canvas.getHeight() - 50, 75, 30);
        flagsManager.setActiveFlag(player.getLanguage());

        lastFrameTime = System.nanoTime();
    }

    private void setupStage(Stage stage) {
        stage.setFullScreen(true);
        stage.show();

        stage.widthProperty().addListener((_, _, _) -> updateCanvasSize(stage));
        stage.heightProperty().addListener((_, _, _) -> updateCanvasSize(stage));
    }

    private void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(e -> handleKeyEvent(e.getCode()));
    }

    // ─── Game Loop ──────────────────────────────────────────────────────
    private void updateGameLoop(long now) {
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);

        backgroundAnimation.update(deltaTime);
        backgroundAnimation.draw(gc);

        updateTextPane();
        flagsManager.draw(gc);
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // ─── Event Handling ─────────────────────────────────────────────────
    private void handleKeyEvent(KeyCode code) {
        switch (code) {
            case L -> handleLanguageSwitch();
            case SPACE -> handleSpaceKey();
            case ESCAPE -> System.exit(0);
            default -> handleAnswerInput(code);
        }
    }

    private void handleLanguageSwitch() {
        if (isGameStarted && !isGameEnded) {
            Language nextLanguage = Language.getNextLanguage(player.getLanguage());
            flagsManager.setActiveFlag(nextLanguage);
            player.setLanguage(nextLanguage);
            updateLanguage();
        }
    }

    private void handleSpaceKey() {
        if (!isGameStarted) {
            startGame();
        } else if (isGameEnded) {
            restartGame();
        } else if (isAnswered) {
            proceedToNextStep();
        }
    }

    private void handleAnswerInput(KeyCode code) {
        if (!isAnswered && isGameStarted && !isGameEnded) {
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

    // ─── Game State Updates ─────────────────────────────────────────────
    private void startGame() {
        isGameStarted = true;
        currentQuestion = inputHandler.getQuestionQuestion();
    }

    private void proceedToNextStep() {
        if (step < TOTAL_STEPS) {
            backgroundAnimation.nextAnimation();
            inputHandler.nextQuestion();
            currentQuestion = inputHandler.getQuestionQuestion();
            currentAnswer = "";
            isAnswered = false;
        } else {
            endGame();
        }
    }

    private void endGame() {
        isGameEnded = true;
        currentQuestion = "";
        currentAnswer = "";
    }

    private void restartGame() {
        player = new Player();
        inputHandler.restartGame(player);
        backgroundAnimation.nextAnimation();

        currentQuestion = "";
        currentAnswer = "";
        isAnswered = false;
        isGameStarted = false;
        isGameEnded = false;
        step = 0;
    }

    private void updateLanguage() {
        currentQuestion = inputHandler.getQuestionQuestion();
        if (isAnswered) {
            try {
                currentAnswer = inputHandler.answerQuestion(input);
            } catch (NotAValidInputException e) {
                e.printStackTrace();
            }
        }
    }

    // ─── GUI Updates ────────────────────────────────────────────────────
    private void updateTextPane() {
        if (questionPane == null) {
            questionPane = new QuestionPane(
                    0f, (float) canvas.getHeight(),
                    (float) canvas.getWidth() * 0.8f,
                    (float) canvas.getHeight() / 6f
            );
            questionPane.setTextAlign(TextAlign.CENTER);
            questionPane.setFrameWidth(4);
            questionPane.setBackgroundOpacity(0.75);
            questionPane.setCornerRadius(60);
            repositionTextPane();
        }

        if (!isGameStarted) {
            questionPane.setQuestion("Press SPACE to start");
        } else if (isGameEnded) {
            questionPane.setQuestion("Quiz complete! Press SPACE to restart");
        } else {
            questionPane.setQuestion(currentQuestion);
        }

        questionPane.setAnswer(currentAnswer);
        questionPane.draw(canvas.getGraphicsContext2D());
    }

    private void updateCanvasSize(Stage stage) {
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight());

        if (questionPane != null) {
            questionPane.setSize(
                    (float) stage.getWidth() * 0.8f,
                    (float) stage.getHeight() / 6f
            );
            repositionTextPane();
        }

        updateFlags();
    }

    private void repositionTextPane() {
        questionPane.setPosition(
                ((float) canvas.getWidth() - questionPane.getWidth()) * 0.5f,
                ((float) canvas.getHeight() - questionPane.getHeight()) * 0.5f
        );
        questionPane.setFontSize(28);
        questionPane.setLineSpacing(12);
        questionPane.setPaddingY(20);
    }

    private void updateFlags() {
        flagsManager.setFlagsPosition(
                canvas.getWidth() - 5 * flagsManager.getFlagWidth() + 10,
                flagsManager.getFlagHeight() - 5
        );
    }
}
