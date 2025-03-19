package ch.fhnw.team6.model;

import java.util.HashMap;
import java.util.Map;



public class Question {
    private final Difficulty difficulty;
    private Map<Language, String> question = new HashMap<>();
    private String correctAnswer = "";
    private Map<Language, String> explanationCorrect = new HashMap<>();
    private Map<Language, String> explanationIncorrect = new HashMap<>();
    private String rightAnswer;

    /**
     * creates new Question instance
     * @param question map with versions of question in 4 predefined languages
     * @param correctAnswer correct answer to the question (in form of barcode string)
     * @param explanationCorrect map with versions of explanation (in case player answered right) in different languages
     * @param explanationIncorrect map with versions of explanation (in case player answered false) in different languages
     * @param difficulty difficulty of a question
     * @throws IllegalArgumentException if any of the parameters is null or not all languages are defined in any of the maps
     * @see Question predefined languages
     */
    public Question(Map<Language, String> question, Map<Language, String> explanationCorrect,
                    Map<Language, String> explanationIncorrect, String correctAnswer, String difficulty) throws IllegalArgumentException{
        if(question == null || correctAnswer == null || explanationCorrect == null || explanationIncorrect == null || difficulty == null){
            throw new IllegalArgumentException("Question not formatted correctly");
        }
        if(question.size() < 4 || explanationCorrect.size() < 4 || explanationIncorrect.size() < 4){
            throw new IllegalArgumentException("not all of the languages are handled");
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
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * returns a question statement in a given language
     * @param language language in which we want to get the question
     */
    public String getQuestion(Language language) {
        return question.get(language);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * returns an explanation (in case answered right) in a given language
     * @param language language in which we want to get explanation
     */
    public String getExplanationCorrect(Language language) {
        return explanationCorrect.get(language);
    }

    /**
     * returns an explanation (in case answered false) in a given language
     * @param language language in which we want to get explanation
     */
    public String getExplanationIncorrect(Language language) {
        return explanationIncorrect.get(language);
    }

}
