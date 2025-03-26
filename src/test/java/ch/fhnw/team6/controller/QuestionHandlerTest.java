package ch.fhnw.team6.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.fhnw.team6.exceptions.NoMoreQuestionsException;
import ch.fhnw.team6.model.Difficulty;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;

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
                Language.FRENCH, "Question Exemple",
                Language.ITALIAN, "Domanda Esempio"
        );

        List<Question> questions = Arrays.asList(
                new Question(sampleText, sampleText, sampleText, "123456", "EASY"),
                new Question(sampleText, sampleText, sampleText, "123457", "EASY"),
                new Question(sampleText, sampleText, sampleText, "123458", "MEDIUM"),
                new Question(sampleText, sampleText, sampleText, "123459", "MEDIUM"),
                new Question(sampleText, sampleText, sampleText, "123460", "HARD"),
                new Question(sampleText, sampleText, sampleText, "123461", "HARD")
        );

        questionHandler = new QuestionHandler(player, questions);
    }

     @Test
    void testGetNextQuestionReturnsCorrectDifficulty() {
        player.setPoints(2); // Should get EASY question
        assertEquals(Difficulty.EASY, questionHandler.getNextQuestion().getDifficulty());

        player.setPoints(5); // Should get MEDIUM question
        assertEquals(Difficulty.MEDIUM, questionHandler.getNextQuestion().getDifficulty());

        player.setPoints(7); // Should get HARD question
        assertEquals(Difficulty.HARD, questionHandler.getNextQuestion().getDifficulty());
    }

    @Test
    void testGetNextQuestionMovesToNextDifficulty() {
        player.setPoints(2); // Get all EASY questions
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

}
