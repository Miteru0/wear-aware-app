package ch.fhnw.team6.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class BackgroundManager {

    private AnimationManager animationManager;
    private Canvas canvas;
    private double scale = 1.0;

    /**
     * Constructor for BackgroundManager
     *
     * @param canvas     The canvas to get the size from
     * @param totalSteps The total number of steps in the animation
     * @param width      The width of the background image
     * @param height     The height of the background image
     */
    public BackgroundManager(Canvas canvas, int totalSteps, int frameCount, double width, double height) {
        this.canvas = canvas;
        this.animationManager = new AnimationManager(0, 0, width, height, "background", totalSteps, frameCount, 4);
        updateScale();
    }

    /**
     * Updates the scale based on the current canvas size
     */
    private void updateScale() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        FrameAnimation frameAnimation = animationManager.getCurrentAnimation();
        double animationCount = frameAnimation.getAnimationCount();

        // Ensure canvas size is valid before calculating the scale
        if (canvasWidth > 0 && canvasHeight > 0 && animationCount > 0) {
            double imageWidth = frameAnimation.getWidth();
            double imageHeight = frameAnimation.getHeight();

            // Calculate scale to fit the image within the canvas
            this.scale = Math.min(
                    canvasWidth / imageWidth,
                    canvasHeight / imageHeight);
        }

    }

    /**
     * Draws the current animation frame on the canvas
     *
     * @param gc The GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {

        updateScale();
        FrameAnimation frame = animationManager.getCurrentAnimation();

        frame.setScale(scale);

        double x = (canvas.getWidth() - frame.getWidth() * scale) / 2;
        double y = (canvas.getHeight() - frame.getHeight() * scale) / 2;

        frame.setX(x);
        frame.setY(y);

        animationManager.draw(gc);
    }

    /**
     * Updates the animation state based on the elapsed time
     * 
     * @param deltaTime The time since the last update
     */
    public void update(double deltaTime) {
        animationManager.update(deltaTime);
    }

    /**
     * Starts the next animation in the sequence
     */
    public void nextAnimation() {
        animationManager.nextAnimation();
    }

}
