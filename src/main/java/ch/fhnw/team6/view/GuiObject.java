package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;

public abstract class GuiObject {

    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Constructor for GuiObject
     *
     * @param x      The x position of the object
     * @param y      The y position of the object
     * @param width  The width of the object
     * @param height The height of the object
     */
    public GuiObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the object on the canvas
     *
     * @param gc The GraphicsContext to draw on
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Updates the object state based on the elapsed time
     *
     * @param deltaTime The time since the last update
     */
    public abstract void update(double deltaTime);

}
