package ch.fhnw.team6.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ClothingTest {

    @Test
    void testValidClothingCreation() {
        Clothing clothing = new Clothing("T-shirt", "12345");
        assertNotNull(clothing);
        assertEquals("T-shirt", clothing.getName());
        assertEquals("12345", clothing.getBarcode());
    }

    @Test
    void testClothingCreationWithNullNameThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Clothing(null, "12345");
        });
        assertEquals("name and barcode cannot be empty", exception.getMessage());
    }

    @Test
    void testClothingCreationWithBlankNameThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Clothing("   ", "12345");
        });
        assertEquals("name and barcode cannot be empty", exception.getMessage());
    }

    @Test
    void testClothingCreationWithNullBarcodeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Clothing("T-shirt", null);
        });
        assertEquals("name and barcode cannot be empty", exception.getMessage());
    }

    @Test
    void testClothingCreationWithBlankBarcodeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Clothing("T-shirt", "   ");
        });
        assertEquals("name and barcode cannot be empty", exception.getMessage());
    }
}
