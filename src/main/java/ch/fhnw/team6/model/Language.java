package ch.fhnw.team6.model;

public enum Language {
    GERMAN,
    ENGLISH,
    FRENCH,
    ITALIAN;

    /**
     * Returns the next Language in the enum
     * Loops back to the first language after the last one
     * 
     * @param current The current language
     * @return The next language
     */
    public static Language getNextLanguage(Language current) {

        int currentIndex = current.ordinal();
        int nextIndex = (currentIndex + 1) % values().length;
        return values()[nextIndex];
        
    }
}
