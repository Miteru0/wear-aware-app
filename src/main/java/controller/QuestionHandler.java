package controller;

import model.Player;
import model.Question;

import java.util.List;

public class QuestionHandler {
    private List<Question> allQuestions;
    private Player player = new Player();

    public String showNewQuestion(){
        //TODO: will return the next question
        return "";
    }

    private boolean isAppropriateQuestion(Question question){
        //TODO: checks, whether or not a question is appropriate or not
        //TODO: checks for language of the player (standard: German)
        return false;
    }

    public void setAllQuestions(List<Question> allQuestions){
        this.allQuestions = allQuestions;
    }
    public Player getPlayer() {
        return player;
    }
}
