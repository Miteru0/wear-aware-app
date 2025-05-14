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
import com.pi4j.io.gpio.digital.DigitalInputConfig;
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {



    // ─── Constants ─────────────────────────────────────────────────────
    private static final int TOTAL_STEPS = 2;
    private static final double WINDOWED_WIDTH = 1280;
    private static final double WINDOWED_HEIGHT = 720;
    private static final Map<Language, String> startScreen = new HashMap<>(Map.of(
            Language.GERMAN, "Hallo, ich bin Beni und begleite dich durch das Spiel. Drücke jetzt den Startknopf, um loszulegen!",
            Language.ENGLISH, "Hello, I am Beni and I will guide you through the game. Press the start button now to get started!",
            Language.FRENCH, "Bonjour, je suis Beni et je vais t'accompagner tout au long du jeu. Appuie maintenant sur le bouton de démarrage pour commencer !"
    ));
    private static final Map<Language, String> endScreen = new HashMap<>(Map.of(
            Language.GERMAN, "ich hoffe, das Spiel hat dir gut gefallen und du was gelernt hast. Drücke jetzt den Startknopf, um das Spiel neuzustarten.",
            Language.ENGLISH, "I hope you enjoyed the game and learned something. Press the start button now to restart the game.",
            Language.FRENCH, "J'espère que le jeu t'a plu et que tu as appris quelque chose. Appuie maintenant sur le bouton de démarrage pour recommencer le jeu."
    ));

    private static Logger logger = Logger.getLogger(Main.class.getName());

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
    private boolean isAnswered = false;
    private boolean isGameStarted = false;
    private boolean isGameEnded = false;
    private int step = 0;
    private long lastFrameTime;
    private String input = "";
//Pin 24 is f*cked
    private Context pi4j = Pi4J.newAutoContext();
    final DigitalInputConfig buttonConfigStop = DigitalInput.newConfigBuilder(pi4j).id("BCM_" + 23)
        .name("buttonStop").address(23)
        .pull(PullResistance.PULL_DOWN)
        .debounce(3000L).build();
    final DigitalInputConfig buttonConfigLanguage = DigitalInput.newConfigBuilder(pi4j).id("BCM_" + 27)
        .name("ButtonLanguage").address(27)
        .pull(PullResistance.PULL_DOWN)
        .debounce(3000L).build(); //0.5 Seconds

    DigitalInput buttonStop = pi4j.create(buttonConfigStop);
    DigitalInput buttonLanguage = pi4j.create(buttonConfigLanguage);

    public void registerListeners() {
        buttonStop.addListener(event -> handleButtonStop(buttonStop.isHigh()));
        buttonLanguage.addListener(event -> handleButtonLanguage(buttonLanguage.isHigh()));
    }
    private void handleButtonStop(boolean pressedState) {
        if(pressedState){
            logger.log(Level.INFO, "game stopped");
        }
    }
    private void handleButtonLanguage(boolean pressedState) {
        if(pressedState){
            logger.log(Level.INFO, "language changed");
        }
    }


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
        backgroundAnimation = new BackgroundManager(canvas, TOTAL_STEPS, 2, 1280, 720);
        mascot = new AnimationManager(WINDOWED_WIDTH * 0.8 - 20, WINDOWED_HEIGHT * 0.6, WINDOWED_WIDTH * 0.2, WINDOWED_HEIGHT * 0.4, "mascot", 1, 4, 4);
        mascot.setCurrentAnimationVisible(false);
        dialogBubble = new DialogBubble(0, 0, 1000, 400);
        dialogBubble.setBubbleImage(ResourceLoader.loadImage("/images/bubble/bubble.png", 1000, 400));
        //dialogBubble.setBubbleImageSize(1000, 400);
        dialogBubble.setVisible(false);
        dialogBubble.setTextAlign(TextAlign.CENTER);
        

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

        mascot.update(deltaTime);
        mascot.draw(gc);

        updateBubble();
        dialogBubble.draw(gc);
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // ─── Event Handling ─────────────────────────────────────────────────
    private void handleKeyEvent(KeyCode code) {
        registerListeners();
        switch (code) {
            case L -> handleLanguageSwitch();
            case SPACE -> handleSpaceKey();
            case ESCAPE -> System.exit(0);
            default -> handleAnswerInput(code);
        }
    }

    private void handleLanguageSwitch() {
        Language nextLanguage = Language.getNextLanguage(player.getLanguage());
        flagsManager.setActiveFlag(nextLanguage);
        player.setLanguage(nextLanguage);
        if (isGameStarted && !isGameEnded) {
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
        mascot.setCurrentAnimationVisible(false);
        dialogBubble.setVisible(false);
    }

    private void handleAnswerInput(KeyCode code) {
        if (!isAnswered && isGameStarted && !isGameEnded) {
            try {
                input = code.getName();
                currentAnswer = inputHandler.answerQuestion(input);
                isAnswered = true;
                mascot.setCurrentAnimationVisible(true);
                dialogBubble.setText(currentAnswer);
                updateBubble();
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
        } else {
            endGame();
        }
        isAnswered = false;
        mascot.setCurrentAnimationVisible(false);  
        updateBubble();
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
        if(inputHandler.getCurrentQuestion() instanceof PercentQuestion) {
            ((PercentQuestion) inputHandler.getCurrentQuestion()).decrementAnswer(input);
        }
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
            dialogBubble.setText(startScreen.get(player.getLanguage()));
            dialogBubble.setVisible(true);
            mascot.setCurrentAnimationVisible(true);
            questionPane.setVisible(false);

        } else if (isGameEnded) {
            dialogBubble.setText(endScreen.get(player.getLanguage()));
            dialogBubble.setVisible(true);
            mascot.setCurrentAnimationVisible(true);
            questionPane.setVisible(false);
        } else {
            questionPane.setVisible(true);
            questionPane.setQuestion(currentQuestion);

        }


        //questionPane.setAnswer(currentAnswer);
        questionPane.draw(canvas.getGraphicsContext2D());
    }

    private void updateMascot() {
        mascot.setCurrentAnimationSize(
            canvas.getWidth()/4,
            canvas.getHeight()/2.5
        );
        mascot.setCurrentAnimationPosition(
                (canvas.getWidth() - mascot.getCurrentAnimationWidth()),
                (canvas.getHeight() - mascot.getCurrentAnimationHeight())
        );     
    }

    private void updateBubble() {
        if(!isGameStarted) {
            dialogBubble.setVisible(true);
//            dialogBubble.setText("Press SPACE to start");
            currentAnswer = "Press SPACE to start!";
        }
        if(isGameEnded) {
            dialogBubble.setVisible(true);
            currentAnswer = " Quiz complete! Press SPACE to restart!";
        }
        if(isAnswered) {
            dialogBubble.setVisible(true);
            dialogBubble.setText(currentAnswer);
        }

        dialogBubble.setSize(
            canvas.getWidth() * 0.5f,
            Math.max(canvas.getHeight()/10,currentAnswer.length() / (canvas.getWidth() / 10) * dialogBubble.getFontSize() * dialogBubble.getLineSpacing())
        );
        dialogBubble.setPosition(
                mascot.getCurrentAnimationX() - dialogBubble.getWidth() + mascot.getCurrentAnimationWidth() / 4.2,
                mascot.getCurrentAnimationY() - dialogBubble.getHeight() + mascot.getCurrentAnimationHeight() / 3.5
        );
        dialogBubble.setTextArea();
    }

    private void updateCanvasSize(Stage stage) {
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight());

        if (questionPane != null) {
            questionPane.setSize(
                    (float) stage.getWidth() * 0.8f,
                    (float) stage.getHeight() / 8f
            );
            repositionTextPane();
        }

        updateFlags();
        updateMascot();
    }

    private void repositionTextPane() {
        questionPane.setPosition(
                ((float) canvas.getWidth() - questionPane.getWidth()) * 0.5f,
                ((float) canvas.getHeight() - questionPane.getHeight()) * 0.3f
        );
        questionPane.setFontSize(28);
        questionPane.setLineSpacing(12);
        questionPane.setPaddingY(30);
    }

    private void updateFlags() {
        flagsManager.setFlagsPosition(
                canvas.getWidth() - 5 * flagsManager.getFlagWidth() + 10,
                flagsManager.getFlagHeight() - 5
        );
    }
}
