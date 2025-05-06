package ch.fhnw.team6.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Question {
    private Difficulty difficulty = Difficulty.EASY;
    private Map<Language, String> question = new HashMap<>();
    private final String id;

    public Question(String difficulty, Map<Language, String> question, String id) {
        if (difficulty.equals("MEDIUM")){
            this.difficulty = Difficulty.MEDIUM;
        } else if(difficulty.equals("HARD")){
            this.difficulty = Difficulty.HARD;
        }
        this.question = question;
        this.id = id;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * returns a question statement in a given language
     * @param language language in which we want to get the question
     */
   public String getQuestion(Language language) {
       return question.getOrDefault(language, question.get(Language.ENGLISH));
   }

    /**
     * returns an explanation (in case answered right) in a given language
     * @param language language in which we want to get explanation
     */
    abstract public String getExplanation(Language language, String answer);

    public String getId() {
        return id;
    }
}
