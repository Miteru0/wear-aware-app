package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import java.util.Scanner;
import java.util.List;

public class InputHandler {
    private List<Clothing> allValidInputs;
    QuestionHandler questionHandler = new QuestionHandler();

    private static void scannerInput(){

    Scanner scanner = new Scanner(System.in);
    int barcode = sc


}
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
        if(answerCorrect){
            questionHandler.getPlayer().setPoints(questionHandler.getPlayer().getPoints() + 1);
        } else {
            questionHandler.getPlayer().setPoints(questionHandler.getPlayer().getPoints() - 1);
        }
    }
}
