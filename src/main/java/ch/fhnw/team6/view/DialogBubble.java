package ch.fhnw.team6.view;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class DialogBubble extends TextPane {

    private Image bubbleImage;
    private double imageOffsetX = 0;
    private double imageOffsetY = 0;

    private double textAreaX = 10;
    private double textAreaY = 20;
    private double textAreaWidth;
    private double textAreaHeight;

    public DialogBubble(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.textAreaWidth = width * 0.9;
        this.textAreaHeight = height * 0.9;
    }

    @Override
    public void draw(GraphicsContext gc) {

        if (!isVisible) {
            return;
        }

        if (bubbleImage != null) {
            gc.save();

            gc.setGlobalAlpha(getBackgroundOpacity());

            gc.drawImage(bubbleImage, getX() + imageOffsetX, getY() + imageOffsetY, getWidth(), getHeight());

            gc.restore();
        }

        // Set font and color for the text
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font(fontSize));

        // Draw wrapped text within the text area
        double textY = getY() + textAreaY + fontSize;
        List<String> wrappedText = wrapText(gc, text, textAreaWidth);
        numberOfLines = wrappedText.size();

        // Draw each wrapped line of text
        for (String line : wrappedText) {
            double textWidth = computeTextWidth(gc, line);
            double textX = calculateAlignedX(line, textWidth);
            gc.fillText(line, textX, textY);
            textY += fontSize + lineSpacing;
        }
    }

    @Override
    protected double calculateAlignedX(String text, double textWidth) {
        // Adjust the alignment based on the text area width
        switch (textAlign) {
            case LEFT:
                return getX() + textAreaX;
            case CENTER:
                return getX() + textAreaX + (textAreaWidth - textWidth) / 2;
            case RIGHT:
                return getX() + textAreaX + textAreaWidth - textWidth;
            default:
                return getX() + textAreaX;
        }
    }

    public void setBubbleImage(Image image) {
        this.bubbleImage = image;
    }

    public void setTextArea() {
        this.textAreaWidth = getWidth() * 0.9;
        this.textAreaHeight = getHeight() * 0.9;
        this.textAreaX = (getWidth() - textAreaWidth) / 2;
        this.textAreaY = (getHeight() - textAreaHeight) / 2;
    }

    public double getTextAreaX() {
        return textAreaX;
    }

    public double getTextAreaY() {
        return textAreaY;
    }

    public double getTextAreaWidth() {
        return textAreaWidth;
    }

    public double getTextAreaHeight() {
        return textAreaHeight;
    }

}
