package ch.fhnw.team6.model;

import java.util.Map;

public class PercentQuestion extends Question{
    private final Map<String, Integer> givenAnswers;
    private final Map<Language, String> explanation;
    private int numberOfAnswers;

    public PercentQuestion(String difficulty, Map<String, Integer> givenAnswers, Map<Language, String> question, Map<Language, String> explanation, int numberOfAnswers, String id) {
        super(difficulty, question, id);
        this.givenAnswers = givenAnswers;
        this.explanation = explanation;
        this.numberOfAnswers = numberOfAnswers;
    }

    @Override
    public String getExplanation(Language language, String answer) {
        givenAnswers.put(answer, givenAnswers.getOrDefault(answer, 0) + 1);
        numberOfAnswers++;

        double percentage = (givenAnswers.get(answer) / (double) numberOfAnswers) * 100;
        String formatted = String.format("%.1f", percentage);

        String base = explanation.getOrDefault(language, explanation.get(Language.ENGLISH));
        return base.replace("Ї", formatted + "%");
    }

}
