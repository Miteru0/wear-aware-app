package ch.fhnw.team6.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(); // Default German player
    }

    @Test
    void testDefaultLanguage() {
        assertEquals(Language.GERMAN, player.getLanguage(), "Default language should be German.");
    }

    @Test
    void testCustomLanguage() {
        Player frenchPlayer = new Player(Language.FRENCH);
        assertEquals(Language.FRENCH, frenchPlayer.getLanguage(), "Player language should be set correctly.");
    }

    @Test
    void testAnswer_Correct() {
        player.answer(true);
        assertEquals(1, player.getPoints(), "Points should increase by 1 on correct answer.");
    }

    @Test
    void testAnswer_Incorrect_NoNegativePoints() {
        player.answer(false);
        assertEquals(0, player.getPoints(), "Points should not go below zero.");
    }

    @Test
    void testAnswer_Incorrect_AfterCorrect() {
        player.answer(true);
        player.answer(false);
        assertEquals(0, player.getPoints(), "Points should decrease by 1 after incorrect answer.");
    }

}
