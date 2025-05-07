package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.view.AnimationManager;
import ch.fhnw.team6.view.FlagsManager;
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

    private static final int TOTAL_STEPS = 7; // number of quiz questions/animations

    private Canvas canvas;
    private AnimationManager animationManager;
    private InputHandler inputHandler;
    private FlagsManager flagsManager;

    private QuestionPane questionPane;
    private Player player;

    private String currentQuestion;
    private String currentAnswer;
    private boolean isAnswered;
    private boolean isGameStarted;
    private boolean isGameEnded;

    private int step;
    private long lastFrameTime;
    private String input = "";

    @Override
    public void start(Stage primaryStage) {
        initializeGame(primaryStage);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, 1280, 720, Color.BLACK);
        primaryStage.setScene(scene);
        setupStage(primaryStage);

        scene.setOnKeyPressed(e -> handleKeyEvent(e.getCode()));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGameLoop(now);
            }
        }.start();
    }

    private void initializeGame(Stage primaryStage) {
        primaryStage.setTitle("Animation Test");

        player = new Player();
        canvas = new Canvas(1280, 720);
        inputHandler = new InputHandler(player);
        animationManager = new AnimationManager(canvas, TOTAL_STEPS, 4);
        flagsManager = new FlagsManager(canvas.getWidth()-4*75, canvas.getHeight() - 50, 75, 30);
        flagsManager.setActiveFlag(player.getLanguage());

        currentQuestion = "";
        currentAnswer = "";
        isAnswered = false;
        isGameStarted = false;
        isGameEnded = false;
        step = 0;
        lastFrameTime = System.nanoTime();
    }

    private void setupStage(Stage primaryStage) {
        primaryStage.setFullScreen(true);
        primaryStage.show();

        primaryStage.widthProperty().addListener((_,_,_) -> updateCanvasSize(primaryStage));
        primaryStage.heightProperty().addListener((_,_,_) -> updateCanvasSize(primaryStage));
    }

    private void updateGameLoop(long now) {
        double deltaTime = Math.min((now - lastFrameTime) / 1_000_000_000.0, 0.1);
        lastFrameTime = now;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);
        animationManager.update(deltaTime);
        animationManager.draw(gc);
        updateTextPane();
        flagsManager.draw(canvas);
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void updateFlags() {
        flagsManager.setFlagPosition(canvas.getWidth() - 5 * flagsManager.getFlagWidth() + 10, flagsManager.getFlagHeight() - 5);
    }

    private void updateTextPane() {
        if (questionPane == null) {
            questionPane = new QuestionPane(
                    0f,
                    (float) canvas.getHeight(),
                    (float) canvas.getWidth() * 0.8f,
                    (float) canvas.getHeight() / 6f
            );
            questionPane.setTextAlign(TextAlign.CENTER);
            questionPane.setFrameWidth(4);
            questionPane.setBackgroundOpacity(0.75);
            questionPane.setPosition(
                    ((float) canvas.getWidth() - questionPane.getWidth()) * 0.5f,
                    ((float) canvas.getHeight() - questionPane.getHeight()) * 0.5f
            );
            questionPane.setCornerRadius(90);
        }

        if (!isGameStarted) {
            questionPane.setQuestion("Press SPACE to start");
            questionPane.setAnswer("");
        } else if (isGameEnded) {
            questionPane.setQuestion("Quiz complete! Press SPACE to restart");
            questionPane.setAnswer("");
        } else {
            questionPane.setQuestion(currentQuestion);
            questionPane.setAnswer(currentAnswer);
        }

        questionPane.draw(canvas);
    }

    private void handleKeyEvent(KeyCode code) {
        switch (code) {
            case L:
                handleLanguageSwitch();
                break;
            case SPACE:
                handleSpaceKey();
                break;
            case ESCAPE:
                System.exit(0);
                break;
            default:
                handleAnswerInput(code);
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

    private void handleSpaceKey() {
        if (!isGameStarted) {
            // Start the game
            isGameStarted = true;
            currentQuestion = inputHandler.getQuestionQuestion();
            return;
        }
        if (isGameEnded) {
            // Restart after end screen
            restartGame();
            return;
        }
        if (isAnswered) {
            if (step < TOTAL_STEPS) {
                // Proceed to next question
                animationManager.nextAnimation();
                inputHandler.nextQuestion();
                currentQuestion = inputHandler.getQuestionQuestion();
                currentAnswer = "";
                isAnswered = false;
            } else {
                isGameEnded = true;
                currentQuestion = "";
                currentAnswer = "";
            }
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

    private void updateCanvasSize(Stage primaryStage) {
        canvas.setWidth(primaryStage.getWidth());
        canvas.setHeight(primaryStage.getHeight());

        if (questionPane != null) {
            questionPane.setSize(
                    (float) primaryStage.getWidth() * 0.8f,
                    (float) primaryStage.getHeight() / 6f
            );
            questionPane.setPosition(
                    ((float) primaryStage.getWidth() - questionPane.getWidth()) * 0.5f,
                    ((float) primaryStage.getHeight() - questionPane.getHeight()) * 0.5f
            );
            questionPane.setFontSize(28);
            questionPane.setLineSpacing(12);
            questionPane.setPaddingY(20);
        }

        updateFlags();
    }

    private void restartGame() {
        player = new Player();
        inputHandler.restartGame(player);
        animationManager.nextAnimation();

        currentQuestion = "";
        currentAnswer = "";
        isAnswered = false;
        isGameStarted = false;
        isGameEnded = false;
        step = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
