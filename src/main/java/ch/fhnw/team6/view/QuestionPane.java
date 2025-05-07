package ch.fhnw.team6.view;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class QuestionPane extends TextPane {

    private String question;
    private String answer;

    /**
     * Constructor for QuestionPane
     * 
     * @param x      The x-coordinate of the pane
     * @param y      The y-coordinate of the pane
     * @param width  The width of the pane
     * @param height The height of the pane
     */
    public QuestionPane(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.question = "";
        this.answer = "";
    }

    /**
     * Sets the question text
     * 
     * @param question The question text to be displayed
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the answer text
     * 
     * @param answer The answer text to be displayed
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Draws the question and answer text on the canvas
     * 
     * @param gc The GraphicsContext used for drawing
     */
    @Override
    public void draw(GraphicsContext gc) {

        // Draw background
        drawBackground(gc);

        // Set font and color
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(fontSize));

        double textY = getY() + paddingY + fontSize;

        // Draw the question text
        drawText(gc, question, textY);

        // Add space between question and answer
        textY += computeTextHeight(gc, question) + lineSpacing * 2;

        // Draw the answer text
        drawText(gc, answer, textY);
    }

    /**
     * Computes the height of the text based on the font size and line spacing
     * 
     * @param gc   The GraphicsContext used for drawing
     * @param text The text to be measured
     * @return The height of the text
     */
    private double computeTextHeight(GraphicsContext gc, String text) {
        List<String> wrappedText = wrapText(gc, text, getWidth() - 20);
        return wrappedText.size() * (fontSize + lineSpacing);
    }

}
