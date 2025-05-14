package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class TextPane extends GuiObject {

    protected String text = "";
    protected int fontSize = 24;
    protected double lineSpacing = 10;
    protected double backgroundOpacity = 0.7;
    protected TextAlign textAlign = TextAlign.CENTER;
    protected TextVAlign textVAlign = TextVAlign.TOP;
    protected double paddingX = 10;
    protected double paddingY = 20;
    protected double cornerRadius = 0;
    protected double frameWidth = 0;
    protected boolean isVisible = true;
    protected double numberOfLines = 0;
    protected boolean withBackground = true;
    protected Color fontColor = Color.WHITE;

    /**
     * Constructor for TextPane
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param width width of the pane
     * @param height height of the pane
     */
    public TextPane(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Update method to be overridden by subclasses
     * 
     * @param deltaTime time since last update
     */
    @Override
    public void update(double deltaTime) {}


    /**
     * Draw method to be overridden by subclasses
     * 
     * @param gc GraphicsContext for drawing
     */
    public void draw(GraphicsContext gc) {
        if (!isVisible) return;

        if (withBackground) {
            drawBackground(gc);
        }

        gc.setFill(fontColor);
        gc.setFont(Font.font(fontSize));

        List<String> wrappedLines = wrapText(gc, text, getWidth() - 2 * paddingX);
        numberOfLines = wrappedLines.size();

        double totalTextHeight = numberOfLines * (fontSize + lineSpacing) - lineSpacing;
        double startY = calculateAlignedY(totalTextHeight);

        for (String line : wrappedLines) {
            double textWidth = computeTextWidth(gc, line);
            double textX = calculateAlignedX(textWidth);
            gc.fillText(line, textX, startY);
            startY += fontSize + lineSpacing;
        }
    }

    /**
     * Draws the background of the pane
     * 
     * @param gc GraphicsContext for drawing
     */
    protected void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(34, 34, 34, backgroundOpacity));
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight(), cornerRadius, cornerRadius);

        if (frameWidth > 0) {
            gc.setLineWidth(frameWidth);
            gc.setStroke(Color.BLACK);
            gc.strokeRoundRect(getX(), getY(), getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }

    /**
     * Calculates the aligned X position based on text alignment
     * 
     * @param textWidth width of the text
     * 
     * @return aligned X position
     */
    protected double calculateAlignedX(double textWidth) {
        switch (textAlign) {
            case LEFT: return getX() + paddingX;
            case CENTER: return getX() + (getWidth() - textWidth) / 2;
            case RIGHT: return getX() + getWidth() - textWidth - paddingX;
            default: return getX() + paddingX;
        }
    }

    /**
     * Calculates the aligned Y position based on text vertical alignment
     * 
     * @param totalTextHeight total height of the text
     * 
     * @return aligned Y position
     */
    protected double calculateAlignedY(double totalTextHeight) {
        switch (textVAlign) {
            case TOP: return getY() + paddingY + fontSize;
            case CENTER: return getY() + (getHeight() - totalTextHeight) / 2 + fontSize;
            case BOTTOM: return getY() + getHeight() - totalTextHeight - paddingY + fontSize;
            default: return getY() + paddingY + fontSize;
        }
    }

    /**
     * Wraps text to fit within the specified width
     * 
     * @param gc GraphicsContext for drawing
     * @param text text to be wrapped
     * @param maxWidth maximum width for wrapping
     * 
     * @return list of wrapped lines
     */
    protected List<String> wrapText(GraphicsContext gc, String text, double maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            double lineWidth = computeTextWidth(gc, testLine);

            if (lineWidth > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
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
     * @param gc GraphicsContext for drawing
     * @param text text to be measured
     * 
     * @return width of the text
     */
    protected double computeTextWidth(GraphicsContext gc, String text) {
        Text tempText = new Text(text);
        tempText.setFont(gc.getFont());
        return tempText.getLayoutBounds().getWidth();
    }

    // Getters and Setters
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    public int getFontSize() { return fontSize; }
    public void setLineSpacing(double spacing) { this.lineSpacing = spacing; }
    public double getLineSpacing() { return lineSpacing; }
    public void setBackgroundOpacity(double opacity) { this.backgroundOpacity = opacity; }
    public void setTextAlign(TextAlign align) { this.textAlign = align; }
    public void setTextVAlign(TextVAlign align) { this.textVAlign = align; }
    public void setText(String text) { this.text = text; }
    public void setCornerRadius(double radius) { this.cornerRadius = radius; }
    public void setPaddingX(double paddingX) { this.paddingX = paddingX; }
    public void setPaddingY(double paddingY) { this.paddingY = paddingY; }
    public void setFrameWidth(double frameWidth) { this.frameWidth = frameWidth; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public void setWithBackground(boolean withBackground) { this.withBackground = withBackground; }
    public void setFontColor(Color color) { this.fontColor = color; }
    public Color getFontColor() { return fontColor; }
    public int getNumberOfLines() { return (int) numberOfLines; }
}
