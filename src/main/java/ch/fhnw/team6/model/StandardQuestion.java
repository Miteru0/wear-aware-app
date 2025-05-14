package ch.fhnw.team6.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class StandardQuestion extends Question {
    private Map<Language, String> question = new HashMap<>();
    private List<String> correctAnswer = new ArrayList<>();
    private Map<Language, String> explanationCorrect = new HashMap<>();
    private Map<Language, String> explanationIncorrect = new HashMap<>();

    /**
     * creates new Question instance
     * @param question map with versions of question in 4 predefined languages
     * @param correctAnswer correct answer to the question (in form of barcode string)
     * @param explanationCorrect map with versions of explanation (in case player answered right) in different languages
     * @param explanationIncorrect map with versions of explanation (in case player answered false) in different languages
     * @param difficulty difficulty of a question
     * @throws IllegalArgumentException if any of the parameters is null or not all languages are defined in any of the maps
     * @see StandardQuestion predefined languages
     */
    public StandardQuestion(Map<Language, String> question, Map<Language, String> explanationCorrect,
                            Map<Language, String> explanationIncorrect, List<String> correctAnswer, String difficulty, String id) throws IllegalArgumentException{
        super(difficulty, question, id);
        if(correctAnswer == null || explanationCorrect == null || explanationIncorrect == null || difficulty == null){
            throw new IllegalArgumentException("Question not formatted correctly");
        }
        if(explanationCorrect.size() < 3 || explanationIncorrect.size() < 3){
            throw new IllegalArgumentException("not all of the languages are handled");
        }
        this.correctAnswer = correctAnswer;
        this.explanationCorrect = explanationCorrect;
        this.explanationIncorrect = explanationIncorrect;


    }

    public List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public Map<Language, String> getExplanation() {
        return question;
    }

    @Override
    public String getExplanation(Language language, String answer) {
        return correctAnswer.contains(answer)? explanationCorrect.get(language): explanationIncorrect.get(language);
    }


}
