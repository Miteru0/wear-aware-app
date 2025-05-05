package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;

public class FadeTransition implements Animation {

    private final FrameAnimation out;
    private final FrameAnimation in;
    private double progress;
    private double duration;
    private boolean useEasing;

    /**
     * Constructor for FadeTransition
     *
     * @param out        The animation to fade out
     * @param in         The animation to fade in
     * @param duration   The duration of the transition
     * @param useEasing  Whether to use easing for the transition
     */
    public FadeTransition(FrameAnimation out, FrameAnimation in, double duration, boolean useEasing) {
        this.out = out;
        this.in = in;
        this.duration = duration;
        this.useEasing = useEasing;
    }

    /**
     * Updates the transition progress
     * 
     * @param deltaTime The time since the last update
     */
    @Override
    public void update(double deltaTime) {
        progress = Math.min(progress + deltaTime / duration, 1.0);
        out.update(deltaTime);
        in.update(deltaTime);
    }

    /**
     * Draws the transition
     * 
     * @param gc The GraphicsContext to draw on
     */
    @Override
    public void draw(GraphicsContext gc) {
        double interpProgress = getInterpolatedProgress();

        gc.save();
        gc.setGlobalAlpha(1.0 - interpProgress);
        out.draw(gc);
        gc.restore();

        gc.save();
        gc.setGlobalAlpha(interpProgress);
        in.draw(gc);
        gc.restore();
    }

    /**
     * Gets the interpolated progress for easing
     * 
     * @return The interpolated progress value
     */
    private double getInterpolatedProgress() {
        if (!useEasing) return progress;
        double x = progress;
        return x * x * (3 - 2 * x);
    }

    /**
     * Checks if the transition is complete
     * 
     * @return True if the transition is complete, false otherwise
     */
    public boolean isComplete() {
        return progress >= 1.0;
    }

    /**
     * Resets the transition (not really needed)
     */
    @Override
    public void reset() {
        // we don't really need it, do we?)
    }
}
