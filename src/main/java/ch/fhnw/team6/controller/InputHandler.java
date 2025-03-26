package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import ch.fhnw.team6.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class InputHandler {
    private List<String> allValidInputs;
    private QuestionHandler questionHandler;

    public InputHandler(Player player) {
        questionHandler = new QuestionHandler(player);
    }

    public void setAllValidInputs() {
        //TODO check valid inputs
    }

    public void restartGame(Player player) {
        questionHandler.startNewGame(player);
    }

    public boolean checkInput(String input) {
        //TODO: check if the scanned barcode is valid
        //TODO: check if the scanned barcode is correct
        return false;
    }

    public void pointUpdate(boolean answerCorrect) {
        // TODO: update the point-number of the Player-Object in Question-Handler
        if (answerCorrect) {
            questionHandler.getPlayer().setPoints(questionHandler.getPlayer().getPoints() + 1);
        } else {
            questionHandler.getPlayer().setPoints(questionHandler.getPlayer().getPoints() - 1);
        }
    }
}
