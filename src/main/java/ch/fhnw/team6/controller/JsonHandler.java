package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
    private void loadClothes(){
        //TODO: use GSON to read out JSON-file
        //makes Clothing-Objects
        //Objects are used by the List in Input-Handler --> setAllValidInputs()
        try(JsonReader reader = new JsonReader(new FileReader("C:\\git-repo\\wearAware\\src\\main\\resources\\JSON\\clothes\\clothes.json"))){
            System.out.println("test");
            //input.setAllValidInputs(gson.fromJson(reader, Clothing[].class));
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            gson.fromJson(jsonObject.get("clothing"), new TypeToken<List<Clothing>>(){}.getType());
            //Clothing clothing = gson.fromJson(reader,Clothing.class);
            //System.out.println(clothing);
        }catch (IOException e){
//            throw new RuntimeException(e);
            System.err.println("Error loading Clothes");
        }
    }
    private void loadQuestions(){
        //TODO: use GSON to read out JSON-file
        //makes Question-Objects
        //Objects are used by the List in Question-Handler --> setAllQuestions()
    }
}
