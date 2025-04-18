package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Question;
import ch.fhnw.team6.model.QuestionDTO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler {

    private final static Gson GSON = new Gson();

    /**
     * Reads out the Json-File "clothes" from the folder
     * Turns Json-Data into Objects
     * @throws IOException 
     */
    public static List<Clothing> loadClothes(String jsonPath) throws IOException {
        List<Clothing> clothes = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) { // C:\git-repo\wearAware\src\main\resources\JSON\clothes\clothes.json
            JsonArray jsonArray = GSON.fromJson(reader, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                // String fullString = jsonElement.getAsString();
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                String barcode = jsonElement.getAsJsonObject().get("barcode").getAsString();
                clothes.add(new Clothing(name, barcode));
            }
        } catch (IOException e) {
            System.err.println("Trying production path");
            throw new IOException();
        }
        return clothes;
    }

    public static List<Question> loadQuestions(String jsonPath) throws IOException {
        Map<String, QuestionDTO> questionDTOs = new HashMap<>();
        
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) {
            JsonArray jsonArray = GSON.fromJson(reader, JsonArray.class);
            
            for (JsonElement jsonElement : jsonArray) {
                String id = jsonElement.getAsJsonObject().get("id").getAsString();
                
                if (questionDTOs.containsKey(id)) {
                    updateQuestionDTO(questionDTOs.get(id), jsonElement);
                } else {
                    questionDTOs.put(id, parseQuestionElement(jsonElement));
                }
            }
        } catch (IOException e) {
            System.err.println("Trying production path");
            throw new IOException();
        }
        
        return convertDTOsToQuestions(questionDTOs);
    }

    private static QuestionDTO parseQuestionElement(JsonElement jsonElement) {
        QuestionDTO questionDTO = new QuestionDTO();
        String question = jsonElement.getAsJsonObject().get("question").getAsString();
        Language language = Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString());
        
        questionDTO.addQuestion(question, language);
        questionDTO.setRightClothingBarcode(
            Arrays.asList(jsonElement.getAsJsonObject().get("rightClothingBarcode").getAsString().split(",")));
        questionDTO.addExplanationRight(
            jsonElement.getAsJsonObject().get("explanationRight").getAsString(), language);
        questionDTO.addExplanationWrong(
            jsonElement.getAsJsonObject().get("explanationWrong").getAsString(), language);
        questionDTO.setDifficulty(
            jsonElement.getAsJsonObject().get("difficulty").getAsString());
        
        return questionDTO;
    }

    private static void updateQuestionDTO(QuestionDTO questionDTO, JsonElement jsonElement) {
        String question = jsonElement.getAsJsonObject().get("question").getAsString();
        Language language = Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString());
        
        questionDTO.addQuestion(question, language);
        questionDTO.addExplanationRight(
            jsonElement.getAsJsonObject().get("explanationRight").getAsString(), language);
        questionDTO.addExplanationWrong(
            jsonElement.getAsJsonObject().get("explanationWrong").getAsString(), language);
    }

    private static List<Question> convertDTOsToQuestions(Map<String, QuestionDTO> questionDTOs) {
        List<Question> questions = new ArrayList<>();
        
        for (QuestionDTO questionDTO : questionDTOs.values()) {
            questions.add(new Question(
                questionDTO.getQuestion(),
                questionDTO.getExplanationRight(),
                questionDTO.getExplanationWrong(),
                questionDTO.getRightClothingBarcode(),
                questionDTO.getDifficulty()
            ));
        }
        
        return questions;
    }
    
}
