package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    @Test
    void testLoadClothes_ValidFile() {
        // Arrange
        String testFilePath = "src/test/resources/JSON/clothes/clothes_test.json";

        // Act
        List<Clothing> clothes = JsonHandler.loadClothes(testFilePath);

        // Assert
        assertEquals(4, clothes.size(), "Expected 4 clothing items.");

        assertEquals("T-shirt", clothes.get(0).getName());
        assertEquals("1", clothes.get(0).getBarcode());

        assertEquals("Hose", clothes.get(1).getName());
        assertEquals("2", clothes.get(1).getBarcode());

        assertEquals("Jeans", clothes.get(2).getName());
        assertEquals("3", clothes.get(2).getBarcode());

        assertEquals("Pullover", clothes.get(3).getName());
        assertEquals("4", clothes.get(3).getBarcode());
    }

    @Test
    void testLoadClothes_FileNotFound() {
        String invalidPath = "src/test/resources/JSON/clothes/nonexistent.json";
        List<Clothing> clothes = JsonHandler.loadClothes(invalidPath);
        assertTrue(clothes.isEmpty(), "Clothes list should be empty if file is not found.");
    }

    @Test
    void testLoadQuestions_ValidFile() {
        // Arrange
        String testFilePath = "src/test/resources/JSON/questions/questions_test.json";

        // Act
        List<Question> questions = JsonHandler.loadQuestions(testFilePath);

        // Assert
        assertEquals(1, questions.size(), "Expected 1 unique question after merging languages.");

        Question question = questions.get(0);
        assertEquals("4", question.getCorrectAnswer());
        assertEquals("EASY", question.getDifficulty().name());

        // Check all four languages
        assertEquals("English-Dummy Frage", question.getQuestion(Language.ENGLISH));
        assertEquals("Deutsch-Dummy Frage", question.getQuestion(Language.GERMAN));
        assertEquals("Français-Dummy Frage", question.getQuestion(Language.FRENCH));
        assertEquals("Italiano-Dummy Frage", question.getQuestion(Language.ITALIAN));

        // Explanation check
        assertEquals("TEST TEST TEST", question.getExplanationCorrect(Language.ENGLISH));
        assertEquals("TEST TEST TEST", question.getExplanationIncorrect(Language.ENGLISH));
    }

    @Test
    void testLoadQuestions_FileNotFound() {
        String invalidPath = "src/test/resources/JSON/questions/nonexistent.json";
        List<Question> questions = JsonHandler.loadQuestions(invalidPath);
        assertTrue(questions.isEmpty(), "Questions list should be empty if file is not found.");
    }
}

