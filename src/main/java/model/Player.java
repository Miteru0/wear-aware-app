package model;

public class Player {
    private int points = 0;

    Language language = Language.GERMAN;
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
