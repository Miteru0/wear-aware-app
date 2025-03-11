package controller;

public class JsonReader {
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
