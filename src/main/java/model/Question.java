package model;

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
    private Language language;
    private String question;
    private String correctAnswer;
    private String explanationCorrect;
    private String explanationIncorrect;
    public Question(String question, String correctAnswer, String explanationCorrect, String explanationIncorrect, String difficulty, String language) {
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
        if(language.equals("GERMAN")){
            this.language = Language.GERMAN;
        } else if(language.equals("ENGLISH")){
            this.language = Language.ENGLISH;
        } else if (language.equals("FRENCH")){
            this.language = Language.FRENCH;
        } else {
            this.language = Language.ITALIAN;
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanationCorrect() {
        return explanationCorrect;
    }

    public String getExplanationIncorrect() {
        return explanationIncorrect;
    }

    public Language getLanguage() {
        return language;
    }
}
