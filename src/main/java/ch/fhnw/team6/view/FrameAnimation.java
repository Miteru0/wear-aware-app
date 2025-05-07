package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FrameAnimation extends GuiObject implements Animation {

    private final Image[] frames;
    private final double frameDuration;
    private double accumulator;
    private double scale = 1.0;
    private int currentFrame;

    /**
     * Constructor for FrameAnimation
     *
     * @param x      The x position of the animation
     * @param y      The y position of the animation
     * @param width  The width of the animation
     * @param height The height of the animation
     * @param frames The array of frames for the animation
     * @param fps    The frames per second for the animation
     */
    public FrameAnimation(double x, double y, double width, double height, Image[] frames, double fps) {
        super(x, y, width, height);
        this.frames = frames;
        this.frameDuration = 1.0 / fps;
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
     * Draws the current frame of the animation
     *
     * @param gc The GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {

        Image frame = frames[currentFrame];

        gc.drawImage(frame, getX(), getY(), frame.getWidth() * scale, frame.getHeight() * scale);
    }

    /**
     * Resets the animation to the first frame (not really needed)
     */
    @Override
    public void reset() {
        currentFrame = 0;
        accumulator = 0;
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
     * Checks if the animation is complete, but since this is a looping animation,
     * it will always return false
     * 
     * @return true if the animation is complete, false otherwise
     */
    @Override
    public boolean isComplete() {
        return false; // It's a loop
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getAnimationCount() {
        return frames.length;
    }

}
