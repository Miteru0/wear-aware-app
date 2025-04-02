
package ch.fhnw.team6;

import ch.fhnw.team6.controller.InputHandler;
import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Language;
import ch.fhnw.team6.model.Player;
import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        InputHandler inputHandler = new InputHandler(player);
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = true;
        int level = 0;
        while(true){
            if(gameOver){
                player.setPoints(0);
                inputHandler.restartGame(player);
                gameOver = false;
                level = 1;
            }
            String answer= null;
            while(answer == null){
                System.out.println(inputHandler.getQuestionQuestion());
                try{
                    String input = scanner.nextLine();
                    if(input.equals("d")) {
                        player.setLanguage(Language.GERMAN);
                        continue;
                    };
                    if(input.equals("e")) {
                        player.setLanguage(Language.ENGLISH);
                        continue;
                    }
                    answer = inputHandler.answerQuestion(input);
                }
                catch( NotAValidInputException e){
                    //pass
                }
            }
            System.out.println(answer);

            if(level >= 2){
                gameOver = true;
            }

            level+=1;

        }
    }
}
