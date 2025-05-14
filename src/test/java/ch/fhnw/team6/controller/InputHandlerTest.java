package ch.fhnw.team6.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;
import ch.fhnw.team6.model.StandardQuestion;

public class InputHandlerTest {

    private InputHandler inputHandler;
    private Player player;
    private QuestionHandler questionHandler;
    private ClothingHandler clothingHandler;

    @BeforeEach
    void setUp() {
        // Initialize Player and other components directly
        player = new Player(Language.ENGLISH);

        // Mock questions in multiple languages
        Map<Language, String> sampleText = Map.of(
                Language.ENGLISH, "Sample Question",
                Language.GERMAN, "Beispiel Frage",
                Language.FRENCH, "Question Exemple",
                Language.ITALIAN, "Domanda Esempio");
        // Another one to check right answer
        Map<Language, String> sampleTextExplanationRight = Map.of(
                Language.ENGLISH, "Right!",
                Language.GERMAN, "Richtig!",
                Language.FRENCH, "Idk french, La verdad!",
                Language.ITALIAN, "Same as french, La verdad!");

        // Another one to check wrong ones
        Map<Language, String> sampleTextExplanationWrong = Map.of(
                Language.ENGLISH, "Wrong!",
                Language.GERMAN, "Falsch!",
                Language.FRENCH, "Idk french, Bullshit!",
                Language.ITALIAN, "Same as french, bullshito!");

        List<Question> questions = Arrays.asList(
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "EASY", "1"),
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "EASY", "2"),
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "Medium", "3"),
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "Medium", "4"),
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "Hard", "5"),
                new StandardQuestion(sampleText, sampleTextExplanationRight, sampleTextExplanationWrong, List.of("1"),
                        "Hard", "6"));

        questionHandler = new QuestionHandler(player, questions);

        List<Clothing> testClothes = Arrays.asList(
                new Clothing("T-shirt", "1"),
                new Clothing("Jeans", "2"),
                new Clothing("Jacket", "3"));

        clothingHandler = new ClothingHandler(testClothes);
        inputHandler = new InputHandler(questionHandler, clothingHandler);
    }

    @Test
    void testAnswerQuestionCorrectInput() throws NotAValidInputException {
        String response = inputHandler.answerQuestion("1"); // Correct input
        assertNotNull(response, "Response should not be null.");
        assertEquals("Right!", response, "Expected correct answer explanation.");
    }

    @Test
    void testAnswerQuestionIncorrectInput() throws NotAValidInputException {
        String response = inputHandler.answerQuestion("2"); // Incorrect input
        assertNotNull(response, "Response should not be null.");
        assertEquals("Wrong!", response, "Expected incorrect answer explanation.");
    }

    @Test
    void testAnswerQuestionInvalidInput() {
        assertThrows(NotAValidInputException.class, () -> inputHandler.answerQuestion("invalidBarcode12030214021"),
                "Should throw NotAValidInputException for an invalid input.");
    }

    @Test
    void testGetQuestionQuestion() {
        String questionText = inputHandler.getQuestionQuestion();
        assertNotNull(questionText);
        assertEquals("Sample Question", questionText);
    }

    @Test
    void testNextQuestionChangesCurrentQuestion() {
        Question firstQuestion = inputHandler.getCurrentQuestion();
        inputHandler.nextQuestion();
        Question secondQuestion = inputHandler.getCurrentQuestion();
        assertNotNull(secondQuestion);
        assertNotEquals(firstQuestion, secondQuestion, "Next question should be different from the current one.");
    }

    @Test
    void testRestartGameResetsCurrentQuestion() {
        Question oldQuestion = inputHandler.getCurrentQuestion();
        Player newPlayer = new Player(Language.ENGLISH);
        inputHandler.restartGame(newPlayer);
        Question newQuestion = inputHandler.getCurrentQuestion();
        assertNotNull(newQuestion);
        assertNotEquals(oldQuestion, newQuestion, "After restarting the game, the question should reset.");
    }

    @Test
    void testGetCurrentQuestionNotNull() {
        assertNotNull(inputHandler.getCurrentQuestion(), "Current question should not be null.");
    }

}
