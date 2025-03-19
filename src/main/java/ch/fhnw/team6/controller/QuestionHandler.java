package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Difficulty;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;

import java.util.List;
import java.util.Random;

public class QuestionHandler {
    Random rand = new Random(); // for getNewQuestion

    private List<Question> easyQuestions;
    private List<Question> middleQuestions;
    private List<Question> hardQuestions;
    private final Player player = new Player();

    /**
     * returns a question considering points of a player and removes it from the corresponding list
     * less than 4 points: easy question
     * less than 6 points: medium question
     * else hard question
     * if there is no question of needed difficulty throws exception
     */
    public Question getNewQuestion(){
        List<Question> currentQuestions = hardQuestions;
        if(player.getPoints() < 4) currentQuestions = easyQuestions;
        else if(player.getPoints() < 6) currentQuestions = middleQuestions;
        if(currentQuestions.isEmpty()) throw new IllegalArgumentException();
        Question newQuestion = currentQuestions.get(rand.nextInt(currentQuestions.size()));
        currentQuestions.remove(newQuestion);
        return newQuestion;
    }

    /**
     * sorts allQuestions between easyQuestion, middleQuestions and hardQuestions
     */
    public void sortAllQuestions(List<Question> allQuestions){
        for(Question question : allQuestions){
            if(question.getDifficulty() == Difficulty.EASY){
                easyQuestions.add(question);
            } else if (question.getDifficulty() == Difficulty.MEDIUM){
                middleQuestions.add(question);
            } else{
                hardQuestions.add(question);
            }

        }
    }

    public Player getPlayer() {
        return player;
    }
}
