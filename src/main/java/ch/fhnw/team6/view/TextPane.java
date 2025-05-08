package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextPane extends GuiObject {

    protected String text;
    protected int fontSize = 24;
    protected double lineSpacing = 10;
    protected double backgroundOpacity = 0.7;
    protected TextAlign textAlign = TextAlign.CENTER;
    protected double paddingY = 20;
    protected double cornerRadius = 0;
    protected double frameWidth = 0;
    protected boolean isVisible = true;
    protected double numberOfLines = 0;

    /**
     * Constructor for TextPane
     *
     * @param x      The x-coordinate of the pane
     * @param y      The y-coordinate of the pane
     * @param width  The width of the pane
     * @param height The height of the pane
     */
    public TextPane(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.text = "";
    }

    /**
     * Updates the text pane
     * 
     * @param deltaTime The time since the last update
     */
    @Override
    public void update(double deltaTime) {
        // No specific update logic for text pane
    }

    /**
     * Draws the text pane on the canvas
     * 
     * @param gc The GraphicsContext used for drawing
     */
    public void draw(GraphicsContext gc) {

        // Draw background
        drawBackground(gc);

        // Set font and color
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(fontSize));

        // Draw wrapped text
        double textY = getY() + paddingY + fontSize;
        drawText(gc, text, textY);
    }

    /**
     * Draws the background of the text pane
     * 
     * @param gc The GraphicsContext to draw on
     */
    protected void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(34, 34, 34, backgroundOpacity));
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight(), cornerRadius, cornerRadius);
        gc.setLineWidth(frameWidth);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(getX(), getY(), getWidth(), getHeight(), cornerRadius, cornerRadius);
    }

    /**
     * Draws the text on the canvas
     * 
     * @param gc    The GraphicsContext to draw on
     * @param text  The text to be drawn
     * @param textY The Y-coordinate for the text
     */
    protected void drawText(GraphicsContext gc, String text, double textY) {
        List<String> wrappedText = wrapText(gc, text, getWidth() - 20);
        for (String line : wrappedText) {
            double textWidth = computeTextWidth(gc, line);
            double textX = calculateAlignedX(line, textWidth);
            gc.fillText(line, textX, textY);
            textY += fontSize + lineSpacing;
        }
    }

    /**
     * Calculates the X-coordinate for aligned text
     * 
     * @param text      The text to be aligned
     * @param textWidth The width of the text
     * 
     * @return The aligned X-coordinate
     */
    protected double calculateAlignedX(String text, double textWidth) {
        switch (textAlign) {
            case LEFT:
                return getX() + 10;
            case CENTER:
                return getX() + (getWidth() - textWidth) / 2;
            case RIGHT:
                return getX() + getWidth() - textWidth - 10;
            default:
                return getX();
        }
    }

    /**
     * Wraps the text to fit within the specified width
     * 
     * @param gc       The GraphicsContext to draw on
     * @param text     The text to be wrapped
     * @param maxWidth The maximum width for the text
     * @return
     */
    protected List<String> wrapText(GraphicsContext gc, String text, double maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            double lineWidth = computeTextWidth(gc, testLine);

            if (lineWidth > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append(" ").append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    /**
     * Computes the width of the text
     * 
     * @param gc   The GraphicsContext to draw on
     * @param text The text to be measured
     * 
     * @return The width of the text
     */
    protected double computeTextWidth(GraphicsContext gc, String text) {
        Text tempText = new Text(text);
        tempText.setFont(gc.getFont());
        return tempText.getLayoutBounds().getWidth();
    }

    /**
     * Sets the font size of the text
     * 
     * @param fontSize The font size to be set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the line spacing of the text
     * 
     * @param spacing The line spacing to be set
     */
    public void setLineSpacing(double spacing) {
        this.lineSpacing = spacing;
    }

    public double getLineSpacing() {
        return lineSpacing;
    }

    /**
     * Sets the background opacity of the text pane
     * 
     * @param opacity The opacity to be set
     */
    public void setBackgroundOpacity(double opacity) {
        this.backgroundOpacity = opacity;
    }

    /**
     * Gets the background opacity of the text pane
     * 
     * @return The background opacity
     */
    public double getBackgroundOpacity() {
        return backgroundOpacity;
    }

    /**
     * Sets the text alignment of the text pane
     * 
     * @param align The text alignment to be set
     */
    public void setTextAlign(TextAlign align) {
        this.textAlign = align;
    }

    /**
     * Sets the text to be displayed in the text pane
     * 
     * @param text The text to be set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the corner radius of the text pane
     * 
     * @param cornerRadius The corner radius to be set
     */
    public void setCornerRadius(double cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    /**
     * Sets the padding for the Y-axis
     * 
     * @param paddingY The padding to be set
     */
    public void setPaddingY(double paddingY) {
        this.paddingY = paddingY;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth = frameWidth;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public double getNumberOfLines() {
        return numberOfLines;
    }
}
