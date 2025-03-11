package controller;

import model.Clothing;

import java.util.List;

public class InputHandler {
    private List<Clothing> allValidInputs;

    public void setAllValidInputs(List<Clothing> allValidInputs) {
        this.allValidInputs = allValidInputs;
    }
    public boolean checkInput(String input) {
        //TODO: check if the scanned barcode is valid
        //TODO: check if the scanned barcode is correct
        return false;
    }
    public void pointUpdate(boolean answerCorrect){
        //TODO: update the point-number of the Player-Object in Question-Handler
    }
}
