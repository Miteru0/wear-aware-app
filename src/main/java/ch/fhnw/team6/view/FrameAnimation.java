package ch.fhnw.team6.view;

import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Image;
import io.github.humbleui.types.IRect;

public class FrameAnimation implements Animation {

    private final Image[] frames;
    private final double frameDuration;
    private double accumulator;
    private int currentFrame;
    private final Window window;
    private float scale = 1.0f;

    /**
     * Constructor for FrameAnimation
     * 
     * @param window The window to draw on
     * @param frames Array of images representing the frames of the animation
     * @param fps    Frames per second for the animation
     */
    public FrameAnimation(Window window, Image[] frames, double fps) {
        this.window = window;
        this.frames = frames;
        this.frameDuration = 1.0 / fps;
        calculateScale();
    }

    /**
     * Updates the current frame of the animation
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
     * Calculates the scale of the animation based on the window size
     */
    private void calculateScale() {
        IRect windowRect = window.getWindowRect();
        this.scale = Math.min(
                (float) windowRect.getWidth() / frames[0].getWidth(),
                (float) windowRect.getHeight() / frames[0].getHeight());
    }

    /**
     * Draws the current frame of the animation on the canvas
     * 
     * @param canvas The canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        Image frame = frames[currentFrame];
        canvas.save();
        canvas.translate(
                (window.getWindowRect().getWidth() - frame.getWidth() * scale) / 2f,
                (window.getWindowRect().getHeight() - frame.getHeight() * scale) / 2f);
        canvas.scale(scale, scale);
        canvas.drawImage(frame, 0, 0);
        canvas.restore();
    }

    /**
     * Gets the current frame of the animation
     * 
     * @return The current frame as an Image
     */
    public Image getCurrentFrame() {
        return frames[currentFrame];
    }

    /**
     * Checks if the animation is complete and it's not, because it's a loop
     */
    @Override
    public boolean isComplete() {
        return false;
    }

    /**
     * Resets the animation to the first frame
     */
    @Override
    public void reset() {
        currentFrame = 0;
        accumulator = 0;
    }
}