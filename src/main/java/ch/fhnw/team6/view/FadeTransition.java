package ch.fhnw.team6.view;

import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

public class FadeTransition implements Animation {

    private final Window window;
    private final Animation out;
    private final Animation in;
    private double progress;
    private double duration;
    private boolean useEasing;
    private final float fixedScale;

    /**
     * Constructor for FadeTransition
     * 
     * @param window The window to draw on
     * @param out    The outgoing animation
     * @param in     The incoming animation
     */
    public FadeTransition(Window window, Animation out, Animation in) {
        this(window, out, in, 1.5, true);
    }

    /**
     * Constructor for FadeTransition with custom duration and easing
     * 
     * @param window    The window to draw on
     * @param out       The outgoing animation
     * @param in        The incoming animation
     * @param duration  Duration of the transition in seconds
     * @param useEasing Whether to use easing for the transition
     */
    public FadeTransition(Window window, Animation out, Animation in,
            double duration, boolean useEasing) {
        this.window = window;
        this.out = out;
        this.in = in;
        this.duration = duration;
        this.useEasing = useEasing;

        Image firstFrame = ((FrameAnimation) out).getCurrentFrame();
        IRect windowRect = window.getWindowRect();
        this.fixedScale = Math.min(
                (float) windowRect.getWidth() / firstFrame.getWidth(),
                (float) windowRect.getHeight() / firstFrame.getHeight());
    }

    /**
     * Updates the progress of the transition
     * 
     * @param deltaTime Time since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {

        progress = Math.min(progress + deltaTime / duration, 1.0);

        // Continue updating both animations independently
        out.update(deltaTime);
        in.update(deltaTime);
    }

    /**
     * Draws the transition on the canvas
     * 
     * @param canvas The canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.clear(0x00000000);
        float interpProgress = getInterpolatedProgress();

        try (Paint outPaint = new Paint()) {
            outPaint.setAlphaf(1.0f - interpProgress);
            drawFrame(canvas, ((FrameAnimation) out).getCurrentFrame(), outPaint);
        }

        try (Paint inPaint = new Paint()) {
            inPaint.setAlphaf(interpProgress);
            drawFrame(canvas, ((FrameAnimation) in).getCurrentFrame(), inPaint);
        }
    }

    /**
     * Draws a frame on the canvas with a fixed scale
     * 
     * @param canvas The canvas to draw on
     * @param frame  The image frame to draw
     * @param paint  The paint object for drawing
     */
    private void drawFrame(Canvas canvas, Image frame, Paint paint) {
        canvas.save();
        // Integer positioning to prevent sub-pixel jumps
        int x = (int) ((window.getWindowRect().getWidth() - frame.getWidth() * fixedScale) / 2);
        int y = (int) ((window.getWindowRect().getHeight() - frame.getHeight() * fixedScale) / 2);
        canvas.translate(x, y);
        canvas.scale(fixedScale, fixedScale);
        canvas.drawImage(frame, 0, 0, paint);
        canvas.restore();
    }

    /**
     * Returns the interpolated progress based on the easing function
     * 
     * @return Interpolated progress value
     */
    private float getInterpolatedProgress() {
        if (!useEasing) {
            return (float) progress;
        }
        float x = (float) progress;
        return x * x * (3 - 2 * x);
    }

    /**
     * Returns whether the transition is complete
     * 
     * @return True if the transition is complete, false otherwise
     */
    @Override
    public boolean isComplete() {
        return progress >= 1.0;
    }

    /**
     * Resets the transition to its initial state
     * 
     */
    @Override
    public void reset() {
        progress = 0;
        out.reset();
        in.reset();
    }

    public void setDuration(double seconds) {
        this.duration = seconds;
    }

    public void setEasingEnabled(boolean enabled) {
        this.useEasing = enabled;
    }

    public double getRemainingDuration() {
        return (1.0 - progress) * duration;
    }

}