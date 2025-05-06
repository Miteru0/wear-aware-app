package ch.fhnw.team6.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextPane {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected String text;
    protected int fontSize = 24;
    protected double lineSpacing = 10;
    protected double backgroundOpacity = 0.7;
    protected TextAlign textAlign = TextAlign.CENTER;
    protected double paddingY = 20;

    /**
     * Constructor for TextPane
     *
     * @param x      The x-coordinate of the pane
     * @param y      The y-coordinate of the pane
     * @param width  The width of the pane
     * @param height The height of the pane
     */
    public TextPane(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = "";
    }

    /**
     * Sets the font size of the text
     * 
     * @param fontSize The font size to be set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Sets the line spacing of the text
     * 
     * @param spacing The line spacing to be set
     */
    public void setLineSpacing(double spacing) {
        this.lineSpacing = spacing;
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
     * Sets the padding for the Y-axis
     * 
     * @param paddingY The padding to be set
     */
    public void setPaddingY(double paddingY) {
        this.paddingY = paddingY;
    }

    /**
     * Sets the position of the text pane
     * 
     * @param x The x-coordinate to be set
     * @param y The y-coordinate to be set
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the size of the text pane
     * 
     * @param width  The width to be set
     * @param height The height to be set
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the current X-coordinate of the text pane
     * 
     * @return The current X-coordinate of the text pane
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the current Y-coordinate of the text pane
     * 
     * @return The current Y-coordinate of the text pane
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the current text of the text pane
     * 
     * @return The current text of the text pane
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the current height of the text pane
     * 
     * @return The current height of the text pane
     */
    public float getHeight() {
        return height;
    }

    /**
     * Draws the text pane on the canvas
     * 
     * @param canvas The canvas on which to draw
     */
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw background
        drawBackground(gc);

        // Set font and color
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(fontSize));

        // Draw wrapped text
        double textY = y + paddingY + fontSize;
        drawText(gc, text, textY);
    }

    /**
     * Draws the background of the text pane
     * 
     * @param gc The GraphicsContext to draw on
     */
    protected void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(34, 34, 34, backgroundOpacity));
        gc.fillRect(x, y, width, height);
    }

    /**
     * Draws the text on the canvas
     * 
     * @param gc    The GraphicsContext to draw on
     * @param text  The text to be drawn
     * @param textY The Y-coordinate for the text
     */
    protected void drawText(GraphicsContext gc, String text, double textY) {
        List<String> wrappedText = wrapText(gc, text, width - 20);
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
    private double calculateAlignedX(String text, double textWidth) {
        switch (textAlign) {
            case LEFT:
                return x + 10;
            case CENTER:
                return x + (width - textWidth) / 2;
            case RIGHT:
                return x + width - textWidth - 10;
            default:
                return x;
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
    private double computeTextWidth(GraphicsContext gc, String text) {
        Text tempText = new Text(text);
        tempText.setFont(gc.getFont());
        return tempText.getLayoutBounds().getWidth();
    }
}
