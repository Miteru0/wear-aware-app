package ch.fhnw.team6.view;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.fhnw.team6.controller.ResourceLoader;
import ch.fhnw.team6.model.Language;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FlagsManager {

    private final Map<Language, Flag> flags = new LinkedHashMap<>();
    private double flagStartX = 0;
    private double flagY = 0;
    private double flagWidth = 200;
    private double flagHeight = 100;
    private double flagSpacing = 10;

    /**
     * Constructor for FlagsManager
     * 
     * @param flagStartX The starting x-coordinate for the flags
     * @param flagY      The y-coordinate for the flags
     * @param flagWidth  The width of the flags
     * @param flagHeight The height of the flags
     */
    public FlagsManager(double flagStartX, double flagY, double flagWidth, double flagHeight) {
        var images = ResourceLoader.loadFlagImages(flagWidth, flagHeight);
        this.flagWidth = flagWidth;
        this.flagHeight = flagHeight;
        this.flagStartX = flagStartX;
        this.flagY = flagY;
        initializeFlags(images);
    }

    /**
     * Initializes the flags with the given images
     * 
     * @param images A map of Language to Image
     */
    private void initializeFlags(Map<Language, Image> images) {

        int i = 0;

        for (var entry : images.entrySet()) {
            Language lang = entry.getKey();
            Image image = entry.getValue();
            Flag flag = new Flag(lang.toString(), image, flagStartX + i * (flagWidth + flagSpacing), flagY, flagWidth,
                    flagHeight);
            flags.put(lang, flag);
            i++;
        }

    }

    /**
     * Sets the size of the flags
     * 
     * @param width  The width of the flags
     * @param height The height of the flags
     */
    public void setFlagsSize(double width, double height) {
        this.flagWidth = width;
        this.flagHeight = height;

        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            flag.setWidth(width);
            flag.setHeight(height);
        }
    }

    /**
     * Sets the position of the flags
     * 
     * @param x The x-coordinate of the flags
     * @param y The y-coordinate of the flags
     */
    public void setFlagsPosition(double x, double y) {
        this.flagStartX = x;
        this.flagY = y;

        int i = 0;

        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            flag.setX(flagStartX + i * (flagWidth + flagSpacing));
            flag.setY(flagY);
            i++;
        }
    }

    /**
     * Sets the active flag based on the given language
     * 
     * @param lang The language to set as active
     */
    public void setActiveFlag(Language lang) {
        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            if (flag.getName().equals(lang.toString())) {
                flag.setSelected(true);
            } else {
                flag.setSelected(false);
            }
        }
    }

    /**
     * Draws the flags on the canvas
     * 
     * @param gc The GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {

        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            flag.draw(gc);
        }

    }

    public double getFlagStartX() {
        return flagStartX;
    }

    public double getFlagY() {
        return flagY;
    }

    public double getFlagWidth() {
        return flagWidth;
    }

    public double getFlagHeight() {
        return flagHeight;
    }

    public double getFlagSpacing() {
        return flagSpacing;
    }

    public void setFlagSpacing(double flagSpacing) {
        this.flagSpacing = flagSpacing;
    }

}
