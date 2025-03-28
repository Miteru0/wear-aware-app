package ch.fhnw.team6.model;

public class Player {

    private int points = 0;

    private Language language = Language.GERMAN;


    /**
     * Default constructor for the Player
     * Default language is German
     */
    public Player() {

    }

    public Player(Language language) {
        this.language = language;
    }

    public void answer(boolean isCorrect) {
        if (isCorrect) {
            points++;
        } else {
            if (points != 0) {
                points--;
            }
        }
    }

    public int getPoints() {
        return points;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Language getLanguage() {
        return language;
    }
}
