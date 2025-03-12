package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonReader {
    private final Gson gson = new Gson();
    public void setUpData(){
        loadClothes();
        loadQuestions();
    }
    private void loadClothes(){
        //TODO: use GSON to read out JSON-file
        //makes Clothing-Objects
        //Objects are used by the List in Input-Handler --> setAllValidInputs()
        try(Reader reader = new FileReader("src/main/resources/clothes.json")){
            Clothing clothing = gson.fromJson(reader,Clothing.class);
            System.out.println(clothing);
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
