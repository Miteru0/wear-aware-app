package ch.fhnw.team6.model;

 public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public Difficulty nextDifficulty() {
        switch (this) {
            case EASY:
                return MEDIUM;
            case MEDIUM:
                return HARD;
            case HARD:
                return EASY;  // Optionally, if you want it to loop back to EASY
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
