package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Flag extends GuiObject {

    private String name;
    private Image image;
    private boolean isSelected;

    /**
     * Constructor for Flag
     *
     * @param name   The name of the flag
     * @param image  The image of the flag
     * @param x      The x-coordinate of the flag
     * @param y      The y-coordinate of the flag
     * @param width  The width of the flag
     * @param height The height of the flag
     */
    public Flag(String name, Image image, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.name = name;
        this.image = image;
        this.isSelected = false;
    }

    /**
     * Draws the flag on the canvas
     * 
     * @param gc The GraphicsContext to draw on
     */
    @Override
    public void draw(GraphicsContext gc) {

        gc.drawImage(image, getX(), getY(), getWidth(), getHeight());

        if (isSelected) {
            gc.setStroke(javafx.scene.paint.Color.YELLOW);
            gc.setLineWidth(5);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }

    }

    /**
     * Updates the flag's state
     *
     * @param deltaTime The time since the last update
     */
    @Override
    public void update(double deltaTime) {
        // No specific update logic for flags
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
