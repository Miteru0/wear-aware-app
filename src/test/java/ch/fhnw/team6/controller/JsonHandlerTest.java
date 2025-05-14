package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.PercentQuestion;
import ch.fhnw.team6.model.Question;
import ch.fhnw.team6.model.StandardQuestion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    @Test
    void testLoadClothesValidFile() throws IOException {
        // Arrange
        String testFilePath = "src/test/resources/json/clothes_test.json";

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
    void testLoadClothesFileNotFound() throws IOException {
        String invalidPath = "src/test/resources/json/nonexistent.json";
        assertThrows(IOException.class, () -> JsonHandler.loadClothes(invalidPath));
    }

    @Test
    void testLoadQuestionsValidFile() throws IOException {
        // Arrange
        String testFilePath = "src/test/resources/json/questions_test.json";

        // Act
        List<Question> questions = JsonHandler.loadQuestions(testFilePath);

        // Assert
        assertEquals(8, questions.size(), "Expected 1 unique question after merging languages.");

        StandardQuestion question = (StandardQuestion) questions.get(0);
        assertTrue(sameListAs(question.getCorrectAnswer(), List.of("4", "1")));
        assertEquals("EASY", question.getDifficulty().name());

        // Check all four languages
        assertEquals("English-Dummy Frage", question.getQuestion(Language.ENGLISH));
        assertEquals("Deutsch-Dummy Frage", question.getQuestion(Language.GERMAN));
        assertEquals("Français-Dummy Frage", question.getQuestion(Language.FRENCH));
    }

    @Test
    void testLoadPercentQuestions() throws IOException {
        String testFilePath = "src/test/resources/json/percent_questions_test.json";

        List<Question> questions = JsonHandler.loadQuestions(testFilePath);

        assertEquals(1, questions.size());

        PercentQuestion percentQuestion = (PercentQuestion) questions.get(0);
        assertEquals("EASY", percentQuestion.getDifficulty().name());
        assertEquals(8, percentQuestion.getNumberOfAnswers());
    }

    @Test
    void testUpdatePercentAnswer() throws IOException {
        String jsonPath = "src/test/resources/json/percent_questions_update_test.json";

        // Save the current state of the JSON file
        List<Question> questions = JsonHandler.loadQuestions(jsonPath);
        PercentQuestion q = (PercentQuestion) questions.get(0);
        int initialCount = q.getNumberOfAnswers();
        int initialBarcodeCount = q.getGivenAnswers().get("2");

        // Update existing barcode
        JsonHandler.updatePercentAnswer(jsonPath, "10", "2");

        // Reload and check if number of answers increased and barcode count updated
        List<Question> questionsNew = JsonHandler.loadQuestions(jsonPath);
        PercentQuestion qNew = (PercentQuestion) questionsNew.get(0);

        assertEquals(initialCount + 1, qNew.getNumberOfAnswers());
        assertEquals(initialBarcodeCount + 1, qNew.getGivenAnswers().get("2")); // Assuming it was 2 before
    }

    @Test
    void testLoadQuestionsMalformedJson() {
        String badFilePath = "src/test/resources/json/broken_questions.json";
        assertThrows(Exception.class, () -> JsonHandler.loadQuestions(badFilePath));
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
