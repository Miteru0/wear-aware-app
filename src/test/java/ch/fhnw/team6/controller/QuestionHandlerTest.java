package ch.fhnw.team6.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.fhnw.team6.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.fhnw.team6.exceptions.NoMoreQuestionsException;

public class QuestionHandlerTest {

    private QuestionHandler questionHandler;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();

        // Mock questions in multiple languages
        Map<Language, String> sampleText = Map.of(
                Language.ENGLISH, "Sample Question",
                Language.GERMAN, "Beispiel Frage",
                Language.FRENCH, "Question Exemple");

        List<Question> questions = Arrays.asList(
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123456"), "EASY", "1"),
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123457"), "EASY", "2"),
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123458"), "MEDIUM", "3"),
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123459"), "MEDIUM", "4"),
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123460"), "HARD", "5"),
                new StandardQuestion(sampleText, sampleText, sampleText, List.of("123461"), "HARD", "6"));

        questionHandler = new QuestionHandler(player, questions);
    }

    @Test
    void testGetNextQuestionReturnsCorrectDifficulty() {
        player.setPoints(1); // Should get EASY question
        assertEquals(Difficulty.EASY, questionHandler.getNextQuestion().getDifficulty());

        player.setPoints(3); // Should get MEDIUM question
        assertEquals(Difficulty.MEDIUM, questionHandler.getNextQuestion().getDifficulty());

        player.setPoints(5); // Should get HARD question
        assertEquals(Difficulty.HARD, questionHandler.getNextQuestion().getDifficulty());
    }

    @Test
    void testGetNextQuestionMovesToNextDifficulty() {
        player.setPoints(0); // Get all EASY questions
        questionHandler.getNextQuestion();
        questionHandler.getNextQuestion();

        // Should move to MEDIUM since EASY is empty
        Question question = questionHandler.getNextQuestion();
        assertEquals(Difficulty.MEDIUM, question.getDifficulty());
    }

    @Test
    void testNoMoreQuestionsExceptionThrown() {
        // Remove all questions
        while (true) {
            try {
                questionHandler.getNextQuestion();
            } catch (NoMoreQuestionsException e) {
                break;
            }
        }
        assertThrows(NoMoreQuestionsException.class, () -> questionHandler.getNextQuestion());
    }

    @Test
    void testStartNewGameResetsQuestions() {
        questionHandler.getNextQuestion(); // Use up some questions
        Player newPlayer = new Player();
        questionHandler.startNewGame(newPlayer);

        // Ensure all difficulties are restored
        assertEquals(2, questionHandler.getSizeOnDifficulty(Difficulty.EASY));
        assertEquals(2, questionHandler.getSizeOnDifficulty(Difficulty.MEDIUM));
        assertEquals(2, questionHandler.getSizeOnDifficulty(Difficulty.HARD));
    }

    @Test
    void testGetSizeOnDifficultyAfterConsumption() {
        player.setPoints(0); // EASY difficulty
        questionHandler.getNextQuestion();
        questionHandler.getNextQuestion();

        assertEquals(0, questionHandler.getSizeOnDifficulty(Difficulty.EASY));
    }

    @Test
    void testMultipleSequentialQuestions() {
        for (int i = 0; i < 6; i++) {
            questionHandler.getNextQuestion();
        }
        assertThrows(NoMoreQuestionsException.class, () -> questionHandler.getNextQuestion());
    }

}
