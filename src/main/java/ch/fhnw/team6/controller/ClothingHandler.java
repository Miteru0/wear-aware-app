package ch.fhnw.team6.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.fhnw.team6.model.Clothing;

public class ClothingHandler {

    private final String CLOTHES_PATH = "src/main/resources/JSON/clothes/clothes.json";

    private List<Clothing> allClothes = new ArrayList<>();
    private List<String> allInputs = new ArrayList<>();

    /**
     * Constructor for ClothingHandler. It loads the list of all clothing items from the specified JSON file
     * and initializes the list of valid inputs (barcodes).
     */
    public ClothingHandler() {
        allClothes = JsonHandler.loadClothes(CLOTHES_PATH);
        setAllValidInputs();
    }

    /**
     * Secondary constructor used for testing purposes. This constructor allows passing a predefined list of
     * clothing items for testing without loading data from the JSON file.
     * 
     * @param clothes A list of predefined clothing items for testing.
     */
    public ClothingHandler(List<Clothing> clothes) {
        allClothes = clothes;
        setAllValidInputs();
    }

    /**
     * This method sets the list of all valid inputs (barcodes) by extracting the barcode from each clothing item.
     */
    private void setAllValidInputs() {
        allInputs = allClothes.stream()
                .map(clothing -> clothing.getBarcode())
                .collect(Collectors.toList());
    }

    /**
     * Returns a new list containing all valid input barcodes (i.e., barcodes of all clothing items).
     * 
     * @return List of valid input barcodes.
     */
    public List<String> getAllInputs() {
        return new ArrayList<>(allInputs);
    }

}
