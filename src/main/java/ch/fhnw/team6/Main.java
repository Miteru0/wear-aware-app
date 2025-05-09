package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.controller.ResourceLoader;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.PercentQuestion;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.view.*;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {

    // ─── Constants ─────────────────────────────────────────────────────
    private static final int TOTAL_STEPS = 2;
    private static final double WINDOWED_WIDTH = 1280;
    private static final double WINDOWED_HEIGHT = 720;

    // ─── Logger ────────────────────────────────────────────────────────
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // ─── Hardware / GPIO ───────────────────────────────────────────────
    private final Context pi4j = Pi4J.newAutoContext();
    private final DigitalInput buttonStop = createButton(23, "buttonStop", 3000L);
    private final DigitalInput buttonLanguage = createButton(27, "buttonLanguage", 3000L);

    // ─── GUI Components ─────────────────────────────────────────────────
    private Canvas canvas;
    private BackgroundManager backgroundAnimation;
    private AnimationManager mascot;
    private DialogBubble dialogBubble;
    private FlagsManager flagsManager;
    private QuestionPane questionPane;

    // ─── Game Logic ─────────────────────────────────────────────────────
    private InputHandler inputHandler;
    private Player player;

    // ─── Game State ─────────────────────────────────────────────────────
    private String currentQuestion = "";
    private String currentAnswer = "";
    private String input = "";
    private boolean isAnswered = false;
    private boolean isGameStarted = false;
    private boolean isGameEnded = false;
    private int step = 0;
    private long lastFrameTime;

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
        primaryStage.setTitle("Quiz Game");

        player = new Player();
        inputHandler = new InputHandler(player);
        canvas = new Canvas(WINDOWED_WIDTH, WINDOWED_HEIGHT);

        backgroundAnimation = new BackgroundManager(canvas, TOTAL_STEPS, 2, 1280, 720);
        mascot = new AnimationManager(WINDOWED_WIDTH * 0.8 - 20, WINDOWED_HEIGHT * 0.6, WINDOWED_WIDTH * 0.2, WINDOWED_HEIGHT * 0.4, "mascot", 1, 4, 4);
        mascot.setCurrentAnimationVisible(false);

        dialogBubble = new DialogBubble(0, 0, 1000, 400);
        dialogBubble.setBubbleImage(ResourceLoader.loadImage("/images/bubble/bubble.png", 1000, 400));
        dialogBubble.setWithBackground(false);
        dialogBubble.setVisible(false);
        dialogBubble.setTextAlign(TextAlign.CENTER);
        dialogBubble.setTextVAlign(TextVAlign.CENTER);
        dialogBubble.setFontColor(Color.BLACK);

        flagsManager = new FlagsManager(canvas.getWidth() - 4 * 75, canvas.getHeight() - 50, 75, 30);
        flagsManager.setActiveFlag(player.getLanguage());

        lastFrameTime = System.nanoTime();
        registerButtonListeners();
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

    // ─── GPIO Handling ─────────────────────────────────────────────────

    private DigitalInput createButton(int address, String name, long debounce) {
        var config = DigitalInput.newConfigBuilder(pi4j)
            .id("BCM_" + address)
            .name(name)
            .address(address)
            .pull(PullResistance.PULL_DOWN)
            .debounce(debounce)
            .build();
        return pi4j.create(config);
    }

    private void registerButtonListeners() {
        buttonStop.addListener(event -> handleButtonStop(event.state().isHigh()));
        buttonLanguage.addListener(event -> handleButtonLanguage(event.state().isHigh()));
    }

    private void handleButtonStop(boolean pressed) {
        if (pressed) logger.info("Game stopped.");
    }

    private void handleButtonLanguage(boolean pressed) {
        if (pressed) logger.info("Language changed.");
    }

    // ─── Game Loop ─────────────────────────────────────────────────────

    private void updateGameLoop(long now) {
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);

        updateAndDrawBackground(deltaTime, gc);
        updateAndDrawUI(gc);
        updateAndDrawMascot(deltaTime, gc);
    }

    private void updateAndDrawBackground(double deltaTime, GraphicsContext gc) {
        backgroundAnimation.update(deltaTime);
        backgroundAnimation.draw(gc);
    }

    private void updateAndDrawUI(GraphicsContext gc) {
        updateTextPane();
        flagsManager.draw(gc);
        updateBubble();
        dialogBubble.draw(gc);
    }

    private void updateAndDrawMascot(double deltaTime, GraphicsContext gc) {
        mascot.update(deltaTime);
        mascot.draw(gc);
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
            Language nextLang = Language.getNextLanguage(player.getLanguage());
            player.setLanguage(nextLang);
            flagsManager.setActiveFlag(nextLang);
            updateLanguage();
        }
    }

    private void handleSpaceKey() {
        if (!isGameStarted) startGame();
        else if (isGameEnded) restartGame();
        else if (isAnswered) proceedToNextStep();

        mascot.setCurrentAnimationVisible(false);
        dialogBubble.setVisible(false);
    }

    private void handleAnswerInput(KeyCode code) {
        if (isAnswered || !isGameStarted || isGameEnded) return;
        try {
            input = code.getName();
            currentAnswer = inputHandler.answerQuestion(input);
            isAnswered = true;
            mascot.setCurrentAnimationVisible(true);
            dialogBubble.setText(currentAnswer);
            step++;
        } catch (NotAValidInputException e) {
            logger.warning("Invalid input: " + e.getMessage());
        }
    }

    // ─── Game State Management ─────────────────────────────────────────

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
        } else {
            endGame();
        }
        isAnswered = false;
        mascot.setCurrentAnimationVisible(false);
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
        resetGameFlags();
    }

    private void resetGameFlags() {
        isGameStarted = false;
        isGameEnded = false;
        isAnswered = false;
        step = 0;
        currentQuestion = "";
        currentAnswer = "";
    }

    private void updateLanguage() {
        currentQuestion = inputHandler.getQuestionQuestion();
        if (inputHandler.getCurrentQuestion() instanceof PercentQuestion pQ)
            pQ.decrementAnswer(input);
        if (isAnswered) {
            try {
                currentAnswer = inputHandler.answerQuestion(input);
            } catch (NotAValidInputException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    // ─── GUI Updates ────────────────────────────────────────────────────

    private void updateTextPane() {
        if (questionPane == null) {
            questionPane = new QuestionPane(0f, (float) canvas.getHeight(),
                    (float) canvas.getWidth() * 0.8f, (float) canvas.getHeight() / 20f);
            questionPane.setTextAlign(TextAlign.CENTER);
            questionPane.setFrameWidth(4);
            questionPane.setBackgroundOpacity(0.75);
            questionPane.setCornerRadius(60);
            questionPane.setTextVAlign(TextVAlign.CENTER);
            repositionTextPane();
        }

        if (!isGameStarted) {
            dialogBubble.setText("Press SPACE to start");
            dialogBubble.setVisible(true);
            mascot.setCurrentAnimationVisible(true);
            questionPane.setVisible(false);
        } else if (isGameEnded) {
            dialogBubble.setText("Quiz complete! Press SPACE to restart");
            dialogBubble.setVisible(true);
            mascot.setCurrentAnimationVisible(true);
            questionPane.setVisible(false);
        } else {
            questionPane.setVisible(true);
            questionPane.setQuestion(currentQuestion);
        }
        questionPane.draw(canvas.getGraphicsContext2D());
    }

    private void updateMascot() {
        mascot.setCurrentAnimationSize(canvas.getWidth() / 4, canvas.getHeight() / 2.5);
        mascot.setCurrentAnimationPosition(canvas.getWidth() - mascot.getCurrentAnimationWidth(),
                canvas.getHeight() - mascot.getCurrentAnimationHeight());
    }

    private void updateBubble() {
        if (!isGameStarted) currentAnswer = "Press SPACE to start!";
        if (isGameEnded) currentAnswer = "Quiz complete! Press SPACE to restart!";

        if (isAnswered || !isGameStarted || isGameEnded) {
            dialogBubble.setVisible(true);
            dialogBubble.setText(currentAnswer);
        }

        dialogBubble.setPaddingX(40);
        dialogBubble.setSize(canvas.getWidth() * 0.5f,
                Math.max(canvas.getHeight() / 10, dialogBubble.getNumberOfLines() * 50 / (canvas.getWidth() / 10)
                        * dialogBubble.getFontSize() * dialogBubble.getLineSpacing()));
        dialogBubble.setPosition(mascot.getCurrentAnimationX() - dialogBubble.getWidth() + mascot.getCurrentAnimationWidth() / 4.2,
                mascot.getCurrentAnimationY() - dialogBubble.getHeight() + mascot.getCurrentAnimationHeight() / 3.5);
        dialogBubble.setImageOffsetY(dialogBubble.getHeight() / 8);
    }

    private void updateCanvasSize(Stage stage) {
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight());

        if (questionPane != null) {
            questionPane.setSize((float) stage.getWidth() * 0.8f, (float) stage.getHeight() / 8f);
            repositionTextPane();
        }
        updateFlags();
        updateMascot();
    }

    private void repositionTextPane() {
        questionPane.setPosition(((float) canvas.getWidth() - questionPane.getWidth()) * 0.5f,
                ((float) canvas.getHeight() - questionPane.getHeight()) * 0.3f);
        questionPane.setFontSize(28);
        questionPane.setLineSpacing(12);
        questionPane.setPaddingY(30);
    }

    private void updateFlags() {
        flagsManager.setFlagsPosition(canvas.getWidth() - 5 * flagsManager.getFlagWidth() + 10,
                flagsManager.getFlagHeight() - 5);
    }
}
