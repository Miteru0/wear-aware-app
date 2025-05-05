package ch.fhnw.team6.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FrameAnimation implements Animation {

    private final Image[] frames;
    private final double frameDuration;
    private double accumulator;
    private int currentFrame;
    private final Canvas canvas;
    private double scale = 1.0;

    /**
     * Constructor for FrameAnimation
     *
     * @param canvas The canvas on which the animation will be drawn
     * @param frames The frames of the animation
     * @param fps    The frames per second for the animation
     */
    public FrameAnimation(Canvas canvas, Image[] frames, double fps) {
        this.canvas = canvas;
        this.frames = frames;
        this.frameDuration = 1.0 / fps;

        updateScale();
    }

    /**
     * Updates the animation state based on the elapsed time
     *
     * @param deltaTime The time since the last update
     */
    @Override
    public void update(double deltaTime) {
        accumulator += deltaTime;
        while (accumulator >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            accumulator -= frameDuration;
        }
    }

    /**
     * Updates the scale based on the current canvas size
     */
    private void updateScale() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        
        // Ensure canvas size is valid before calculating the scale
        if (canvasWidth > 0 && canvasHeight > 0 && frames.length > 0) {
            double imageWidth = frames[0].getWidth();
            double imageHeight = frames[0].getHeight();
            
            // Calculate scale to fit the image within the canvas
            this.scale = Math.min(
                canvasWidth / imageWidth,
                canvasHeight / imageHeight
            );
        }
    }

    /**
     * Draws the current frame of the animation
     *
     * @param gc The GraphicsContext to draw on
     */
    @Override
    public void draw(GraphicsContext gc) {
        // Ensure the scale is up-to-date before drawing
        updateScale();

        Image frame = frames[currentFrame];
        double x = (canvas.getWidth() - frame.getWidth() * scale) / 2;
        double y = (canvas.getHeight() - frame.getHeight() * scale) / 2;

        // Draw the image with the calculated scale
        gc.drawImage(frame, x, y, frame.getWidth() * scale, frame.getHeight() * scale);
    }

    /**
     * Gets the current frame of the animation
     * 
     * @return The current frame of the animation
     */
    public Image getCurrentFrame() {
        return frames[currentFrame];
    }

    /**
     * Checks if the animation is complete, but since this is a looping animation, it will always return false
     * 
     * @return true if the animation is complete, false otherwise
     */
    @Override
    public boolean isComplete() {
        return false; // It's a loop
    }

    /**
     * Resets the animation to the first frame (not really needed)
     */
    @Override
    public void reset() {
        currentFrame = 0;
        accumulator = 0;
    }
}
