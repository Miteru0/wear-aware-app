package ch.fhnw.team6.controller;

import ch.fhnw.team6.exceptions.NoMoreQuestionsException;
import ch.fhnw.team6.model.Difficulty;
import ch.fhnw.team6.model.Player;
import ch.fhnw.team6.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class QuestionHandler {

    private final Random rand = ThreadLocalRandom.current(); // For randomly generated questions (ThreadLocalRandom is better for multiple thread applications)

    private Map<Difficulty, List<Question>> allQuestions = new HashMap<>(); // Stores all loaded questions, grouped by difficulty
    private Map<Difficulty, List<Question>> currentQuestions;   // Stores remaining questions for the current game session

    private final String QUESTIONS_PATH = "src/main/resources/JSON/questions/Questions.json";   // Path to JSON file containing questions

    private Player player;

    /**
     * Constructor initializes the game by loading questions and setting up the game.
     * @param player The player for whom the questions will be assigned.
     */
    public QuestionHandler(Player player) {
        this.player = player;
        List<Question> questions = JsonHandler.loadQuestions(QUESTIONS_PATH);
        allQuestions = sortAllQuestions(questions);
        createQuestionsForGame();
    }

    /**
     * Constructor for testing purposes only!
     * @param player The player for whom the questions will be assigned.
     * @param questions The list with unsorted questions
     */
    public QuestionHandler(Player player, List<Question> questions) {
        this.player = player;
        allQuestions = sortAllQuestions(questions);
        createQuestionsForGame();
    }

    /**
     * Creates a fresh copy of the question list for a new game session.
     * Ensures that modifications during gameplay do not affect the original question pool.
     */
    public void createQuestionsForGame() {
        currentQuestions = allQuestions.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey, 
                e -> e.getValue().stream().collect(Collectors.toList()) // Deep copy lists
            ));
    }

    /**
     * Retrieves the next question based on the player's score.
     * Removes the selected question from the available questions to avoid repetition.
     * @return The next question for the player.
     * @throws NoMoreQuestionsException if no questions are available.
     */
    public Question getNextQuestion() {
        checkQuestions();
        Difficulty questionDifficulty = getDifficultyFromPoints();
        while (currentQuestions.get(questionDifficulty).isEmpty()) {
            questionDifficulty = questionDifficulty.nextDifficulty();
        }
        int random = rand.nextInt(currentQuestions.get(questionDifficulty).size());
        Question nextQuestion = currentQuestions.get(questionDifficulty).get(random);
        currentQuestions.get(questionDifficulty).remove(random);
        return nextQuestion;
    }

    /**
     * Checks if there are any remaining questions.
     * Throws an exception if no questions are left.
     * @throws NoMoreQuestionsException when all questions are used up.
     */
    private void checkQuestions() {
        if (currentQuestions.get(Difficulty.EASY).isEmpty() &&
                currentQuestions.get(Difficulty.MEDIUM).isEmpty() &&
                currentQuestions.get(Difficulty.HARD).isEmpty()) {
            throw new NoMoreQuestionsException("No more questions available for the player.");
        }
    }

    /**
     * Determines the difficulty level of the next question based on the player's points.
     * @return The appropriate difficulty level.
     */
    private Difficulty getDifficultyFromPoints() {
        if (player.getPoints() < 4) {
            return Difficulty.EASY;
        } else if (player.getPoints() < 6) {
            return Difficulty.MEDIUM;
        } else {
            return Difficulty.HARD;
        }
    }

    /**
     * Starts a new game session with a fresh set of questions and a new player.
     * @param player The new player for the session.
     */
    public void startNewGame(Player player) {
        this.player = player;
        createQuestionsForGame();
    }

    /**
     * Groups and sorts all questions by difficulty.
     * @param allQuestions List of all available questions.
     * @return A map grouping questions by difficulty level.
     */
    public Map<Difficulty, List<Question>> sortAllQuestions(List<Question> allQuestions) {
        return allQuestions.stream()
                .collect(Collectors.groupingBy(Question::getDifficulty));
    }

    /**
     * Method returns size of the list of question depending on Difficulty provided
     * For testing purposes only
     * @param difficulty difficulty to find correct list in the map
     * @return int size of the list
     */
    public int getSizeOnDifficulty(Difficulty difficulty) {
        return currentQuestions.get(difficulty).size();
    }

    public Player getPlayer() {
        return player;
    }
}
