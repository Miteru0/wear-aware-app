package ch.fhnw.team6.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Flag {

    private String name;
    private Image image;
    private boolean isSelected;
    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Constructor for Flag
     *
     * @param name   The name of the flag
     * @param image  The image of the flag
     * @param x     The x-coordinate of the flag
     * @param y     The y-coordinate of the flag
     * @param width  The width of the flag
     * @param height The height of the flag
     */
    public Flag(String name, Image image, double x, double y, double width, double height) {
        this.name = name;
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSelected = false;
    }

    /**
     * Draws the flag on the given canvas
     *
     * @param canvas The canvas on which to draw the flag
     */
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        gc.drawImage(image, x, y, width, height);

        if (isSelected) {
            gc.setStroke(javafx.scene.paint.Color.YELLOW);
            gc.setLineWidth(5);
            gc.strokeRect(x, y, width, height);
        }
        
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



    public Image getImage() {
        return image;
    }



    public void setImage(Image image) {
        this.image = image;
    }

}
