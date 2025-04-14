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

    private final static Gson gson = new Gson();

    /**
     * Reads out the Json-File "clothes" from the folder
     * Turns Json-Data into Objects
     */
    public static List<Clothing> loadClothes(String jsonPath) {
        List<Clothing> clothes = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) { // C:\git-repo\wearAware\src\main\resources\JSON\clothes\clothes.json
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                // String fullString = jsonElement.getAsString();
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                String barcode = jsonElement.getAsJsonObject().get("barcode").getAsString();
                clothes.add(new Clothing(name, barcode));
            }
        } catch (IOException e) {
            System.err.println("Error loading Clothes");
        }
        return clothes;
    }

    public static List<Question> loadQuestions(String jsonPath) {
        // makes Question-Objects
        // Objects are used by the List in Question-Handler --> setAllQuestions()
        List<Question> questions = new ArrayList<>();
        Map<String, QuestionDTO> questionDTOs = new HashMap<>();
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) { // C:\git-repo\wearAware\src\main\resources\JSON\clothes\clothes.json
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                String id = jsonElement.getAsJsonObject().get("id").getAsString();
                if (questionDTOs.containsKey(id)) {
                    QuestionDTO questionDTO = questionDTOs.get(id);
                    String question = jsonElement.getAsJsonObject().get("question").getAsString();
                    questionDTO.addQuestion(question,
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                    questionDTO.addExplanationRight(jsonElement.getAsJsonObject().get("explanationRight").getAsString(),
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                    questionDTO.addExplanationWrong(jsonElement.getAsJsonObject().get("explanationWrong").getAsString(),
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                } else {
                    QuestionDTO questionDTO = new QuestionDTO();
                    String question = jsonElement.getAsJsonObject().get("question").getAsString();
                    questionDTO.addQuestion(question,
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                    questionDTO.setRightClothingBarcode(
                            Arrays.asList(jsonElement.getAsJsonObject().get("rightClothingBarcode").getAsString().split(",")));
                    questionDTO.addExplanationRight(jsonElement.getAsJsonObject().get("explanationRight").getAsString(),
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                    questionDTO.addExplanationWrong(jsonElement.getAsJsonObject().get("explanationWrong").getAsString(),
                            Language.valueOf(jsonElement.getAsJsonObject().get("language").getAsString()));
                    questionDTO.setDifficulty(jsonElement.getAsJsonObject().get("difficulty").getAsString());
                    questionDTOs.put(id, questionDTO);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading Questions");
        }
        for (String questionDTOId : questionDTOs.keySet()) {
            QuestionDTO questionDTO = questionDTOs.get(questionDTOId);
            questions.add(new Question(questionDTO.getQuestion(), questionDTO.getExplanationRight(),
                    questionDTO.getExplanationWrong(), questionDTO.getRightClothingBarcode(),
                    questionDTO.getDifficulty()));
        }
        return questions;
    }

    
}
