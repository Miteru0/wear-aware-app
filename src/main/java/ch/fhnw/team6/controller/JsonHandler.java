package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler {

    private final static Gson GSON = new Gson();

    public static List<Clothing> loadClothes(String jsonPath) throws IOException {
        List<Clothing> clothes = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) {
            JsonArray jsonArray = GSON.fromJson(reader, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                JsonObject obj = jsonElement.getAsJsonObject();
                String name = obj.get("name").getAsString();
                String barcode = obj.get("barcode").getAsString();
                clothes.add(new Clothing(name, barcode));
            }
        } catch (IOException e) {
            System.err.println("Trying production path");
            throw e;
        }
        return clothes;
    }

    /**
     * Auto-detects question type by "type" field and parses accordingly.
     */
    public static List<Question> loadQuestions(String jsonPath) throws IOException {
        List<Question> questions = new ArrayList<>();

        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) {
            JsonArray jsonArray = GSON.fromJson(reader, JsonArray.class);
            Map<String, StandartQuestionDTO> standardDTOs = new HashMap<>();
            Map<String, PercentQuestionDTO> percentDTOs = new HashMap<>();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String type = obj.has("type")
                        ? obj.get("type").getAsString().toLowerCase()
                        : "standard";
                String id = obj.get("id").getAsString();

                if ("percent".equals(type)) {
                    if (!percentDTOs.containsKey(id)) {
                        percentDTOs.put(id, parsePercentQuestionElement(obj));
                    }
                } else {
                    if (standardDTOs.containsKey(id)) {
                        updateStandardDTO(standardDTOs.get(id), obj);
                    } else {
                        standardDTOs.put(id, parseStandardQuestionElement(obj));
                    }
                }
            }

            for (StandartQuestionDTO dto : standardDTOs.values()) {
                questions.add(new StandardQuestion(
                        dto.getQuestion(),
                        dto.getExplanationRight(),
                        dto.getExplanationWrong(),
                        dto.getRightClothingBarcode(),
                        dto.getDifficulty(),
                        dto.getId()
                ));
            }
            for (PercentQuestionDTO dto : percentDTOs.values()) {
                questions.add(new PercentQuestion(
                        dto.getDifficulty(),
                        dto.getGivenAnswers(),
                        dto.getQuestion(),
                        dto.getExplanation(),
                        dto.getNumberOfAnswers(),
                        dto.getId()
                ));
            }
        } catch (IOException e) {
            System.err.println("Trying production path");
            throw e;
        }

        return questions;
    }

    private static StandartQuestionDTO parseStandardQuestionElement(JsonObject obj) {
        StandartQuestionDTO dto = new StandartQuestionDTO();
        String id = obj.get("id").getAsString();
        dto.setId(id);
        Language lang = Language.valueOf(obj.get("language").getAsString());
        dto.addQuestion(obj.get("question").getAsString(), lang);
        dto.setRightClothingBarcode(
                Arrays.asList(obj.get("rightClothingBarcode").getAsString().split(","))
        );
        dto.addExplanationRight(obj.get("explanationRight").getAsString(), lang);
        dto.addExplanationWrong(obj.get("explanationWrong").getAsString(), lang);
        dto.setDifficulty(obj.get("difficulty").getAsString());
        return dto;
    }

    private static void updateStandardDTO(StandartQuestionDTO dto, JsonObject obj) {
        Language lang = Language.valueOf(obj.get("language").getAsString());
        dto.addQuestion(obj.get("question").getAsString(), lang);
        dto.addExplanationRight(obj.get("explanationRight").getAsString(), lang);
        dto.addExplanationWrong(obj.get("explanationWrong").getAsString(), lang);
    }

    private static PercentQuestionDTO parsePercentQuestionElement(JsonObject obj) {
        PercentQuestionDTO dto = new PercentQuestionDTO();
        String id = obj.get("id").getAsString();
        dto.setId(id);
        Language lang = Language.valueOf(obj.get("language").getAsString());
        dto.addQuestion(obj.get("question").getAsString(), lang);
        dto.addExplanation(obj.get("explanation").getAsString(), lang);
        dto.setDifficulty(obj.get("difficulty").getAsString());
        dto.setNumberOfAnswers(obj.get("numberOfAnswers").getAsInt());

        JsonObject given = obj.getAsJsonObject("givenAnswers");
        Map<String, Integer> map = new HashMap<>();
        for (var entry : given.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsInt());
        }
        dto.setGivenAnswers(map);
        return dto;
    }
    public static void updatePercentAnswer(String jsonPath, String questionId, String answerBarcode) throws IOException {
        JsonArray array;
        try (JsonReader reader = new JsonReader(new FileReader(jsonPath))) {
            array = GSON.fromJson(reader, JsonArray.class);
        }

        for (JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();
            if ("percent".equalsIgnoreCase(obj.get("type").getAsString())
                    && questionId.equals(obj.get("id").getAsString())) {


                int total = obj.get("numberOfAnswers").getAsInt() + 1;
                obj.addProperty("numberOfAnswers", total);

                JsonObject given = obj.getAsJsonObject("givenAnswers");
                int previous = given.has(answerBarcode)
                        ? given.get(answerBarcode).getAsInt()
                        : 0;
                given.addProperty(answerBarcode, previous + 1);
            }
        }

        try (Writer writer = new FileWriter(jsonPath)) {
            GSON.toJson(array, writer);
        }
    }

}