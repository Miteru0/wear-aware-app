package ch.fhnw.team6.model;

import java.util.HashMap;
import java.util.Map;

public class PercentQuestionDTO {
    private final Map<Language, String> question = new HashMap<>();
    private final Map<Language, String> explanation = new HashMap<>();
    private Map<String, Integer> givenAnswers = new HashMap<>();
    private int numberOfAnswers;
    private String difficulty;
    private String id;

    public void addQuestion(String text, Language lang) {
        question.put(lang, text);
    }
    public void addExplanation(String text, Language lang) {
        explanation.put(lang, text);
    }
    public void setGivenAnswers(Map<String, Integer> givenAnswers) {
        this.givenAnswers = givenAnswers;
    }
    public void setNumberOfAnswers(int num) {
        this.numberOfAnswers = num;
    }
    public void setDifficulty(String diff) {
        this.difficulty = diff;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Map<Language, String> getQuestion() { return question; }
    public Map<Language, String> getExplanation() { return explanation; }
    public Map<String, Integer> getGivenAnswers() { return givenAnswers; }
    public int getNumberOfAnswers() { return numberOfAnswers; }
    public String getDifficulty() { return difficulty; }
    public String getId() { return id; }
}
