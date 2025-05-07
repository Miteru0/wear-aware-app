package ch.fhnw.team6.view;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.fhnw.team6.controller.ResourceLoader;
import ch.fhnw.team6.model.Language;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class FlagsManager {

    private final Map<Language, Flag> flags = new LinkedHashMap<>();
    private double flagStartX = 0;
    private double flagY = 0;
    private double flagWidth = 200;
    private double flagHeight = 100;
    private double flagSpacing = 10;

    public FlagsManager(double flagStartX, double flagStartY, double flagWidth, double flagHeight) {
        var images = ResourceLoader.loadFlagImages(flagWidth, flagHeight);
        this.flagWidth = flagWidth;
        this.flagHeight = flagHeight;
        this.flagStartX = flagStartX;
        this.flagY = flagStartY;
        initializeFlags(images);
    }

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

    public void setFlagSize(double width, double height) {
        this.flagWidth = width;
        this.flagHeight = height;

        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            flag.setWidth(width);
            flag.setHeight(height);
        }
    }

    public void setFlagPosition(double x, double y) {
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

    public void draw(Canvas canvas) {

        for (var entry : flags.entrySet()) {
            Flag flag = entry.getValue();
            flag.draw(canvas);
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
