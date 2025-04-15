package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    @Test
    void testLoadClothesValidFile() {
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
    void testLoadClothesFileNotFound() {
        String invalidPath = "src/test/resources/JSON/clothes/nonexistent.json";
        List<Clothing> clothes = JsonHandler.loadClothes(invalidPath);
        assertTrue(clothes.isEmpty(), "Clothes list should be empty if file is not found.");
    }

    @Test
    void testLoadQuestionsValidFile() {
        // Arrange
        String testFilePath = "src/test/resources/JSON/questions/questions_test.json";

        // Act
        List<Question> questions = JsonHandler.loadQuestions(testFilePath);

        // Assert
        assertEquals(8, questions.size(), "Expected 1 unique question after merging languages.");

        Question question = questions.get(0);
        assertTrue(sameListAs(question.getCorrectAnswer(), List.of("4", "1")));
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
    void testLoadQuestionsFileNotFound() {
        String invalidPath = "src/test/resources/JSON/questions/nonexistent.json";
        List<Question> questions = JsonHandler.loadQuestions(invalidPath);
        assertTrue(questions.isEmpty(), "Questions list should be empty if file is not found.");
    }

    public boolean sameListAs(List<String> correct1, List<String> correct2) {
        if (correct1.size() != correct2.size()) {
            return false;
        }
        for (int i = 0; i < correct1.size(); i++) {
            if (!correct1.get(i).equals(correct2.get(i))) {
                return false;
            }
        }
        return true;
    }
}

