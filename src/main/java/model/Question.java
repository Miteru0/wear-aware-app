package model;

enum Difficulty {
    EASY,
    MEDIUM,
    HARD
}

public class Question {
    private Difficulty difficulty;
    private String question;
    private String correctAnswer;
    private String explanationCorrect;
    private String explanationIncorrect;
    public Question(String question, String correctAnswer, String explanationCorrect, String explanationIncorrect, String difficulty) {
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
}
