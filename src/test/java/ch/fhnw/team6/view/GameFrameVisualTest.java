package ch.fhnw.team6.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import ch.fhnw.team6.controller.ClothingHandler;
import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.controller.QuestionHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;

public class GameFrameVisualTest {

    static Player player;
    static QuestionHandler questionHandler;
    static ClothingHandler clothingHandler;
    static InputHandler inputHandler;
    static int level = 1;
    public static void main(String[] args) {

        // Initialize Player and other components directly
        player = new Player(Language.ENGLISH);

        // Mock questions in multiple languages
        Map<Language, String> sampleText = Map.of(
                Language.ENGLISH, "Sample Question",
                Language.GERMAN, "Beispiel Frage",
                Language.FRENCH, "Question Exemple",
                Language.ITALIAN, "Domanda Esempio");
        // Another one to check right answer
        Map<Language, String> sampleExplanationRight = Map.of(
                Language.ENGLISH, "Right!",
                Language.GERMAN, "Richtig!",
                Language.FRENCH, "Idk french, La verdad!",
                Language.ITALIAN, "Same as french, La verdad!");

        // Another one to check wrong ones
        Map<Language, String> sampleExplanationWrong = Map.of(
                Language.ENGLISH, "Wrong!",
                Language.GERMAN, "Falsch!",
                Language.FRENCH, "Idk french, Bullshit!",
                Language.ITALIAN, "Same as french, bullshito!");

        List<Question> questions = Arrays.asList(
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "EASY"),
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "EASY"),
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "Medium"),
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "Medium"),
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "Hard"),
                new Question(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"), "Hard"));

        questionHandler = new QuestionHandler(player, questions);

        List<Clothing> testClothes = Arrays.asList(
                new Clothing("T-shirt", "1"),
                new Clothing("Jeans", "2"),
                new Clothing("Jacket", "3"));

        clothingHandler = new ClothingHandler(testClothes);
        inputHandler = new InputHandler(questionHandler, clothingHandler);
        Map<String, String[]> animationPaths = new HashMap<>();
        animationPaths.put("1", new String[] {
            "src/test/resources/images/background/animation1/1.png",
            "src/test/resources/images/background/animation1/2.png",
            "src/test/resources/images/background/animation1/3.png",
            "src/test/resources/images/background/animation1/4.png"
        });
        animationPaths.put("2", new String[] {
            "src/test/resources/images/background/animation2/0.png"
        });
        animationPaths.put("3", new String[] {
            "src/test/resources/images/background/animation3/0.png"
        });
        animationPaths.put("4", new String[] {
            "src/test/resources/images/background/animation4/0.png"
        });
        animationPaths.put("5", new String[] {
            "src/test/resources/images/background/animation5/0.png"
        });
        animationPaths.put("6", new String[] {
            "src/test/resources/images/background/animation6/0.png"
        });


        BackgroundPanel backgroundPanel = new BackgroundPanel(animationPaths, 300, "1");

        // Create JFrame
        JFrame frame = new JFrame("Visual Test for BackgroundPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Use JLayeredPane to layer buttons over the background
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(800, 600);

        // Set panel size
        backgroundPanel.setSize(800, 600);
        backgroundPanel.setLocation(0, 0);

        JTextField textField = new JTextField();
        textField.setSize(150, 40);
        textField.setLocation((800 - 150) / 2, (600 - 40) / 2 - 30); // Centered

        JLabel label = new JLabel(inputHandler.getQuestionQuestion());
        label.setSize(150, 40);
        label.setLocation((800 - 150) / 2, (600 - 40) / 2 - 70); // Above text field
        textField.addActionListener(_ -> {
            
            String input = textField.getText();
            if (input.equals("restart") || input.equals("r")) {
                player.setPoints(0);
                inputHandler.restartGame(player);
                label.setText(inputHandler.getQuestionQuestion());
                textField.setText("");
                level = 1;
                backgroundPanel.setAnimation("" + level);
                return;
            }
            if(input.equals("next") || input.equals("n")) {
                label.setText(inputHandler.getQuestionQuestion());
                textField.setText("");
                return;
            };
            if(input.equals("d")) {
                player.setLanguage(Language.GERMAN);
                textField.setText("");
                label.setText(inputHandler.getQuestionQuestion());
                return;
            };
            if(input.equals("e")) {
                player.setLanguage(Language.ENGLISH);
                textField.setText("");
                label.setText(inputHandler.getQuestionQuestion());
                return;
            }
            if(input.equals("f")) {
                player.setLanguage(Language.FRENCH);
                textField.setText("");
                label.setText(inputHandler.getQuestionQuestion());
                return;
            }
            if(input.equals("i")) {
                player.setLanguage(Language.ITALIAN);
                textField.setText("");
                label.setText(inputHandler.getQuestionQuestion());
                return;
            }
            try {
                label.setText(inputHandler.answerQuestion(input));
            } catch (NotAValidInputException e1) {
                return;
            }
            level++;
            if (level > 5) {
                player.setPoints(0);
                inputHandler.restartGame(player);
                label.setText(inputHandler.getQuestionQuestion());
                textField.setText("");
                level = 1;
                backgroundPanel.setAnimation("" + level);
                return;
            }
            backgroundPanel.setAnimation("" + level);
            textField.setText("");
        });
        // Add components to layered pane
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(textField, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(label, JLayeredPane.PALETTE_LAYER);

        frame.add(layeredPane);
        frame.setVisible(true);

        backgroundPanel.startAnimation();
    }
}
