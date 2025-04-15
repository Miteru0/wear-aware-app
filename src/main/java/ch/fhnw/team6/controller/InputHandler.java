package ch.fhnw.team6.controller;

import ch.fhnw.team6.exceptions.NotAValidInputException;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;

import java.util.List;

public class InputHandler {

    private List<String> allValidInputs;
    private QuestionHandler questionHandler;
    private ClothingHandler clothingHandler;
    private Question currentQuestion;
    boolean isRightAnswer = false;


    /**
     * Initializes the input handler.
     * - Creates a question handler for the player.
     * - Initializes the clothing handler and loads valid clothing inputs.
     * - Retrieves the first question.
     * 
     * @param player The player playing the game.
     */
    public InputHandler(Player player) {
        questionHandler = new QuestionHandler(player);
        clothingHandler = new ClothingHandler();
        setAllValidInputs();
        nextQuestion();
    }

    /**
     * FOR TEST ONLY!!
     * Initializes the input handler (FOR TEST ONLY!!!!!!!!!!!!!)
     * 
     * @param questionHandler test instance of questionHandler
     * @param clothingHandler test instance of clothingHandler
     */
    public InputHandler(QuestionHandler questionHandler, ClothingHandler clothingHandler) {
        this.questionHandler = questionHandler;
        this.clothingHandler = clothingHandler;
        setAllValidInputs();
        nextQuestion();
    }

    /**
     * The heart and soul of the game logic!
     * Mess with it at your own risk (unless you're feeling brave).
     * 
     * Handles the player's answer, updates their score, and moves to the next
     * question.
     * - Validates the input; if it's invalid, throws NotAValidInputException.
     * - Compares the input against the correct answer.
     * - Returns an explanation depending on whether the answer was right or wrong.
     * - Updates the player's score accordingly.
     * - Loads the next question.
     * 
     * @param input The player's answer input.
     * @return Explanation string based on correctness of the answer.
     * @throws NotAValidInputException if the input is invalid (means input should
     *                                 be ignored in the GUI).
     */
    public String answerQuestion(String input) throws NotAValidInputException {
        boolean isRightAnswer = checkInput(input);
        String explanation = (isRightAnswer)
                ? currentQuestion.getExplanationCorrect(questionHandler.getPlayer().getLanguage())
                : currentQuestion.getExplanationIncorrect(questionHandler.getPlayer().getLanguage());
        pointUpdate(isRightAnswer);
        nextQuestion();
        return explanation;
    }

    /**
     * Retrieves the current question text in the player's preferred language.
     * 
     * @return The question as a string.
     */
    public String getQuestionQuestion() {
        return currentQuestion.getQuestion(questionHandler.getPlayer().getLanguage());
    }

    public String getQuestionAnswer() {
        return (isRightAnswer)
                ? currentQuestion.getExplanationCorrect(questionHandler.getPlayer().getLanguage())
                : currentQuestion.getExplanationIncorrect(questionHandler.getPlayer().getLanguage());
    }

    /**
     * Restarts the game with a new player session.
     * Resets the question handler and retrieves a fresh set of questions.
     * 
     * @param player The player starting a new game session.
     */
    public void restartGame(Player player) {
        questionHandler.startNewGame(player);
        nextQuestion();
    }

    /**
     * Checks whether the given input is valid and correct.
     * - First, ensures the input exists in the list of valid inputs.
     * - Then, compares it against the correct answer of the current question.
     * 
     * @param input The input provided by the player.
     * @return True if the input is correct, otherwise false.
     * @throws NotAValidInputException if the input is not in the valid input list.
     */
    private boolean checkInput(String input) throws NotAValidInputException {
        // Checks if the scanned barcode is valid
        if (!allValidInputs.contains(input)) {
            throw new NotAValidInputException(input + " is not a valid input");
        }
        // Checks if the scanned barcode is correct
        return currentQuestion.getCorrectAnswer().contains(input);
    }

    /**
     * Retrieves the next question from the question handler.
     */
    private void nextQuestion() {
        currentQuestion = questionHandler.getNextQuestion();
    }

    /**
     * Loads all valid clothing inputs from the clothing handler.
     */
    private void setAllValidInputs() {
        allValidInputs = clothingHandler.getAllInputs();
    }

    /**
     * Updates the player's score based on whether their answer was correct.
     * 
     * @param answerCorrect True if the player answered correctly, false otherwise.
     */
    private void pointUpdate(boolean answerCorrect) {
        questionHandler.getPlayer().answer(answerCorrect);
    }

}
