package ch.fhnw.team6.model;

import java.util.HashMap;
import java.util.Map;

enum Difficulty {
    EASY,
    MEDIUM,
    HARD
}

enum Language {
    GERMAN,
    ENGLISH,
    FRENCH,
    ITALIAN
}

public class Question {
    private Difficulty difficulty;
    private Map<Language, String> question = new HashMap<>();
    private Map<Language, String> correctAnswer = new HashMap<>();
    private Map<Language, String> explanationCorrect = new HashMap<>();
    private Map<Language, String> explanationIncorrect = new HashMap<>();
    //private Language language;
    //private String question;
    //private String correctAnswer;
    //private String explanationCorrect;
    //private String explanationIncorrect;
    public Question(Map<Language, String> question, Map<Language, String>correctAnswer, Map<Language, String> explanationCorrect, Map<Language, String> explanationIncorrect, String difficulty) {
        if(question == null || correctAnswer == null || explanationCorrect == null || explanationIncorrect == null || difficulty == null){
            throw new IllegalArgumentException("Question not formatted correctly");
        }
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.explanationCorrect = explanationCorrect;
        this.explanationIncorrect = explanationIncorrect;

        if(difficulty.equals("EASY")){
            this.difficulty = Difficulty.EASY;
        } else if (difficulty.equals("MEDIUM")){
            this.difficulty = Difficulty.MEDIUM;
        } else {
            this.difficulty = Difficulty.HARD;
        }

//        if(language.equals("GERMAN")){
//            this.language = Language.GERMAN;
//        } else if(language.equals("ENGLISH")){
//            this.language = Language.ENGLISH;
//        } else if (language.equals("FRENCH")){
//            this.language = Language.FRENCH;
//        } else {
//            this.language = Language.ITALIAN;
//        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Map<Language, String> getQuestion() {
        return question;
    }

    public Map<Language, String> getCorrectAnswer() {
        return correctAnswer;
    }

    public Map<Language, String> getExplanationCorrect() {
        return explanationCorrect;
    }

    public Map<Language, String> getExplanationIncorrect() {
        return explanationIncorrect;
    }
    
}
