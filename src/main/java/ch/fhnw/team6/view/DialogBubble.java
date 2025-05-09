package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class DialogBubble extends TextPane {

    private Image bubbleImage;
    private double imageOffsetX = 0;
    private double imageOffsetY = -10;

    /**
     * Constructor for DialogBubble
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param width width of the bubble
     * @param height height of the bubble
     */
    public DialogBubble(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Draw method to render the dialog bubble
     * 
     * @param gc GraphicsContext for drawing
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (!isVisible) return;

        if (bubbleImage != null) {
            gc.save();
            gc.setGlobalAlpha(backgroundOpacity);
            gc.drawImage(bubbleImage, getX() + imageOffsetX, getY() + imageOffsetY, getWidth(), getHeight());
            gc.restore();
        }

        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font(fontSize));

        super.draw(gc);
    }

    public void setBubbleImage(Image image) { this.bubbleImage = image; }
    public void setImageOffsetX(double offsetX) { this.imageOffsetX = offsetX; }
    public void setImageOffsetY(double offsetY) { this.imageOffsetY = offsetY; }
}
