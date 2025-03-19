package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Question;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler {
    private final Gson gson = new Gson();
    private final InputHandler input = new InputHandler();
    public void setUpData(){
        loadClothes();
        loadQuestions();
    }

    public static void main(String[] args) {
        JsonHandler reader = new JsonHandler();
        reader.loadClothes();
    }

    /**
     * Reads out the Json-File "clothes" from the folder
     * Turns Json-Data into Objects
     */
    private void loadClothes(){
        //TODO: use GSON to read out JSON-file
        List<Clothing> clothes = new ArrayList<>();
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/JSON/clothes/clothes.json"))){ //C:\git-repo\wearAware\src\main\resources\JSON\clothes\clothes.json
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            for(JsonElement jsonElement : jsonArray){
                //String fullString = jsonElement.getAsString();
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                String barcode = jsonElement.getAsJsonObject().get("barcode").getAsString();
                clothes.add(new Clothing(name,barcode));
            }
        }catch (IOException e){
            System.err.println("Error loading Clothes");
        }
    }
    private void loadQuestions(){
        //TODO: use GSON to read out JSON-file
        //makes Question-Objects
        //Objects are used by the List in Question-Handler --> setAllQuestions()
        List<Question> questions = new ArrayList<>();
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/JSON/questions/Questions.json"))){ //C:\git-repo\wearAware\src\main\resources\JSON\clothes\clothes.json
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            for(JsonElement jsonElement : jsonArray){
                //Id ist x
                //
                Map<Language, String> question = new HashMap<>();
                //questions.add(new Question());
            }
        }catch (IOException e){
            System.err.println("Error loading Clothes");
        }
    }
}
