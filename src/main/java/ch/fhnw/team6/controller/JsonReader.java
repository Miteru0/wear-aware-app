package ch.fhnw.team6.controller;

import com.google.gson.Gson;

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
    }
    private void loadQuestions(){
        //TODO: use GSON to read out JSON-file
        //makes Question-Objects
        //Objects are used by the List in Question-Handler --> setAllQuestions()
    }
}
