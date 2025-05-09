package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class QuestionPane extends TextPane {

    private Color fontColor = Color.WHITE;

    /**
     * Constructor for QuestionPane
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param width width of the pane
     * @param height height of the pane
     */
    public QuestionPane(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Draw method to render the question pane
     * 
     * @param gc GraphicsContext for drawing
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (!isVisible) return;

        drawBackground(gc);
        gc.setFill(fontColor);
        gc.setFont(Font.font(fontSize));

        super.draw(gc);
    }

    public void setQuestion(String question) {
        this.text = question;
    }
}
