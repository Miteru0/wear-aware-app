package ch.fhnw.team6.controller;

import ch.fhnw.team6.model.Clothing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClothingHandlerTest {

    private ClothingHandler clothingHandler;

    private List<Clothing> testClothes;

    @BeforeEach
    void setUp() {
        testClothes = Arrays.asList(
                new Clothing("T-shirt", "12345"),
                new Clothing("Jeans", "67890"),
                new Clothing("Jacket", "11223"));

        clothingHandler = new ClothingHandler(testClothes);
    }

    @Test
    void testGetAllInputs() {
        List<String> expectedInputs = Arrays.asList("12345", "67890", "11223");

        // Verify that the getAllInputs method returns the correct barcodes
        assertEquals(expectedInputs, clothingHandler.getAllInputs());
    }

    @Test
    void testConstructorWithFile() {
        // Verify that that handler will be properly initiated with a json file
        ClothingHandler handlerFromFile = new ClothingHandler();
        assertNotNull(handlerFromFile.getAllInputs());
    }

}
