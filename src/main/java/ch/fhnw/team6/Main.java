package ch.fhnw.team6;

import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.view.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JPanel {
    private Player player;
    private InputHandler inputHandler;
    private String currentQuestion = "Scan to begin.";
    private String currentAnswer = "";
    private int level = 0;
    private boolean gameOver = true;
    private boolean showingExplanation = false;
    private boolean gameFinished = false;
    private String currentAnimationKey = "1";
    private BackgroundPanel backgroundPanel;
    private StringBuilder scanBuffer = new StringBuilder();

    private JLabel questionLabel;
    private JLabel answerLabel;

    public Main() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLayout(null);

        player = new Player();
        inputHandler = new InputHandler(player);

        Map<String, String[]> animationPaths = new HashMap<>();
        animationPaths.put("1", new String[]{"src/main/resources/images/background/animation1/0.png"});
        animationPaths.put("2", new String[]{"src/main/resources/images/background/animation2/0.png"});
        animationPaths.put("3", new String[]{"src/main/resources/images/background/animation3/0.png"});
        animationPaths.put("4", new String[]{"src/main/resources/images/background/animation4/0.png"});

        backgroundPanel = new BackgroundPanel(animationPaths, 300, currentAnimationKey);
        backgroundPanel.setBounds(0, 0, 800, 600);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(800, 600);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBounds(0, 100, 800, 150);
        textPanel.setOpaque(false);

        questionLabel = new JLabel(currentQuestion, SwingConstants.CENTER);
        answerLabel = new JLabel(currentAnswer, SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        answerLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        textPanel.add(questionLabel, BorderLayout.NORTH);
        textPanel.add(answerLabel, BorderLayout.SOUTH);

        layeredPane.add(textPanel, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane);

        JTextField hiddenInput = new JTextField();
        hiddenInput.setBounds(0, 0, 1, 1);
        hiddenInput.setOpaque(false);
        hiddenInput.setBorder(null);
        hiddenInput.setForeground(Color.WHITE);
        this.add(hiddenInput);
        hiddenInput.requestFocusInWindow();

        hiddenInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if ((c == '\n' || c == '\r')) {
                    if (showingExplanation && !gameFinished) {
                        nextQuestion();
                    } else if (gameFinished) {
                        restartGame();
                    }
                    return;
                }

                scanBuffer.append(c);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!showingExplanation && !scanBuffer.isEmpty()) {
                    String input = scanBuffer.toString().trim();
                    scanBuffer.setLength(0);
                    processScan(input);
                }
            }
        });

        // Start the game loop
        new Timer(100, e -> {
            if (gameOver) {
                restartGame();
            }
        }).start();
    }

    private void restartGame() {
        player.setPoints(0);
        inputHandler.restartGame(player);
        level = 1;
        setLevelAnimation("1");
        currentQuestion = inputHandler.getQuestionQuestion();
        currentAnswer = "";
        showingExplanation = false;
        gameOver = false;
        gameFinished = false;
        updateLabels();
    }

    private void processScan(String input) {
        if (input.equalsIgnoreCase("d")) {
            player.setLanguage(Language.GERMAN);
            translateCurrent();
        } else if (input.equalsIgnoreCase("e")) {
            player.setLanguage(Language.ENGLISH);
            translateCurrent();
        } else {
            try {
                currentAnswer = inputHandler.answerQuestion(input);
                showingExplanation = true;
            } catch (NotAValidInputException ex) {
                currentAnswer = "❌ Invalid input.";
                showingExplanation = true;
            }
        }
        updateLabels();
    }

    private void translateCurrent() {
        if (!gameOver) {
            currentQuestion = inputHandler.getQuestionQuestion();
            if (showingExplanation && !gameFinished) {
                currentAnswer = inputHandler.getQuestionQuestion();
            }
            updateLabels();
        }
    }

    private void nextQuestion() {
        level++;
        if (level > 3) {
            // Final screen
            setLevelAnimation("4");
            currentQuestion = "🎉 Well done!";
            currentAnswer = "Press Enter to play again.";
            gameFinished = true;
        } else {
            setLevelAnimation(String.valueOf(level));
            currentQuestion = inputHandler.getQuestionQuestion();
            currentAnswer = "";
        }
        showingExplanation = false;
        updateLabels();
    }

    private void updateLabels() {
        questionLabel.setText(currentQuestion);
        answerLabel.setText(currentAnswer);
        repaint();
    }

    private void setLevelAnimation(String animationKey) {
        this.currentAnimationKey = animationKey;
        backgroundPanel.setAnimation(animationKey);
        backgroundPanel.startAnimation();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backgroundPanel.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scanner Game with Animated Background");
        Main gamePanel = new Main();
        frame.setContentPane(gamePanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            for (Component comp : gamePanel.getComponents()) {
                if (comp instanceof JTextField) {
                    comp.requestFocusInWindow();
                }
            }
        });
    }
}
