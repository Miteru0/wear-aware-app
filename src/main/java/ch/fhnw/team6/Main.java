package ch.fhnw.team6;

import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.view.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel {

    private Player player;
    private InputHandler inputHandler;
    private String currentQuestion = "Scan to begin.";
    private String currentAnswer = "";
    private int level = 0;
    private boolean gameOver = true;
    private boolean showingExplanation = false;
    private boolean gameFinished = false;
    private BackgroundPanel backgroundPanel;
    private StringBuilder scanBuffer = new StringBuilder();

    private JLabel questionLabel;
    private JLabel answerLabel;

    // We'll store the screen dimensions for easier reuse.
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Main() {
        // Set this panel's size to full screen.
        this.setPreferredSize(screenSize);
        this.setSize(screenSize);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();

        player = new Player();
        inputHandler = new InputHandler(player);

        backgroundPanel = new BackgroundPanel();
        // Set the background panel to use the full screen dimensions.
        backgroundPanel.setBounds(0, 0, screenSize.width, screenSize.height);

        // Use JLayeredPane to layer background and text panels.
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(screenSize.width, screenSize.height);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        int textPanelWidth = screenSize.width - 100;
        int textPanelHeight = 150;
        TranslucentPanel textPanel = new TranslucentPanel();
        textPanel.setLayout(new GridBagLayout());
        textPanel.setBounds(50, screenSize.height / 4, textPanelWidth, textPanelHeight);

        questionLabel = new JLabel(currentQuestion, SwingConstants.CENTER);
        answerLabel = new JLabel(currentAnswer, SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        answerLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridy = 0;
        textPanel.add(questionLabel, gbc);
        gbc.gridy = 1;
        textPanel.add(answerLabel, gbc);

        layeredPane.add(textPanel, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane);

        // Hidden input field for scanner input.
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
                if ((c == ' ')) {
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
                if (!showingExplanation && !scanBuffer.isEmpty() && !gameFinished) {
                    String input = scanBuffer.toString().trim();
                    scanBuffer.setLength(0);
                    processScan(input);
                }
            }
        });

        // Start the game loop.
        new Timer(100, _ -> {
            if (gameOver) {
                restartGame();
            }
        }).start();

        backgroundPanel.startAnimation();
    }

    /**
     * A custom panel that draws a semi-transparent rounded rectangle behind its
     * components.
     */
    private class TranslucentPanel extends JPanel {
        private Color backgroundColor = new Color(0, 0, 0, 150); // Semi-transparent black.

        public TranslucentPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2d.dispose();
            super.paintComponent(g);
        }
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
        } else if (input.equalsIgnoreCase("i")) {
            player.setLanguage(Language.ITALIAN);
            translateCurrent();
        } else if (input.equalsIgnoreCase("f")) {
            player.setLanguage(Language.FRENCH);
            translateCurrent();
        } else if (!gameOver) {
            try {
                currentAnswer = inputHandler.answerQuestion(input);
                showingExplanation = true;
            } catch (NotAValidInputException ex) {
                // Just ignore invalid input
            }
        }
        updateLabels();
    }

    private void translateCurrent() {
        if (!gameOver) {
            currentQuestion = inputHandler.getQuestionQuestion();
            if (showingExplanation) {
                currentAnswer = inputHandler.getQuestionAnswer();
            }
        }
        updateLabels();
    }

    private void nextQuestion() {
        level++;
        if (level > 3) { // After 3 levels, show final screen with animation 4.
            setLevelAnimation("4");
            currentQuestion = "🎉 Well done!";
            currentAnswer = "Press Spacebar to play again.";
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
        adjustFontSize(questionLabel);
        adjustFontSize(answerLabel);
        repaint();
    }

    /**
     * Adjusts the font size of the label so that the text fits within a maximum
     * width.
     */
    private void adjustFontSize(JLabel label) {
        int maxWidth = screenSize.width - 120; // Account for horizontal padding.
        int baseSize = 26;
        Font font = label.getFont();
        int newSize = baseSize;
        FontMetrics fm = getFontMetrics(new Font(font.getName(), font.getStyle(), newSize));
        String text = label.getText();
        while (fm.stringWidth(text) > maxWidth && newSize > 12) { // Do not go below font size 12.
            newSize--;
            fm = getFontMetrics(new Font(font.getName(), font.getStyle(), newSize));
        }
        label.setFont(new Font(font.getName(), font.getStyle(), newSize));
    }

    private void setLevelAnimation(String animationKey) {
        // Trigger a smooth transition in the background panel.
        backgroundPanel.startTransitionTo(animationKey);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backgroundPanel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Scanner Game with Animated Background");
            Main gamePanel = new Main();
            frame.setContentPane(gamePanel);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();

            // Make the frame fullscreen exclusive
            if (gd.isFullScreenSupported()) {
                frame.setUndecorated(true);
                gd.setFullScreenWindow(frame);
            } else {
                // Fall back to maximized window if fullscreen is not supported
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }

            // Ensure hidden input gets focus.
            for (Component comp : gamePanel.getComponents()) {
                if (comp instanceof JTextField) {
                    comp.requestFocusInWindow();
                }
            }
        });
    }
}
