package ch.fhnw.team6.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandartQuestionDTO {

    private Map<Language, String> question = new HashMap<>();
    private List<String> rightClothingBarcode;
    private Map<Language, String> explanationRight = new HashMap<>();
    private String difficulty;
    private Map<Language, String> explanationWrong = new HashMap<>();
    private String id;

    public void addQuestion(String question, Language language) {
        this.question.put(language, question);
    }

    public void addExplanationRight(String explanationRight, Language language) {
        this.explanationRight.put(language, explanationRight);
    }

    public void addExplanationWrong(String explanationWrong, Language language) {
        this.explanationWrong.put(language, explanationWrong);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setRightClothingBarcode(List<String> rightClothingBarcode) {
        this.rightClothingBarcode = rightClothingBarcode;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Map<Language, String> getQuestion() {
        return question;
    }

    public List<String> getRightClothingBarcode() {
        return rightClothingBarcode;
    }

    public Map<Language, String> getExplanationRight() {
        return explanationRight;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Map<Language, String> getExplanationWrong() {
        return explanationWrong;
    }
    public String getId() {
        return id;
    }

}
