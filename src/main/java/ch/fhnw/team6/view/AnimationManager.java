package ch.fhnw.team6.view;

import ch.fhnw.team6.controller.ResourceLoader;
import javafx.scene.canvas.GraphicsContext;

public class AnimationManager {

    private final FrameAnimation[] animations;
    private int currentIndex;
    private FadeTransition transition;

    /**
     * Constructor for AnimationManager
     *
     * @param x            The x position of the animations
     * @param y            The y position of the animations
     * @param width        The width of the animations
     * @param height       The height of the animations
     * @param folderName   The folder name where the animation frames are stored
     * @param animationCount The number of animations
     * @param frameCount   The number of frames in each animation
     * @param fps          The frames per second for the animations
     */
    public AnimationManager(double x, double y, double width, double height, String folderName, int animationCount,
            int frameCount, int fps) {
        this.animations = new FrameAnimation[animationCount];
        for (int i = 0; i < animationCount; i++) {
            String name = "animation" + (i + 1);
            animations[i] = new FrameAnimation(x, y, width, height,
                    ResourceLoader.loadAnimationFrames(folderName, name, frameCount, width, height), 4);
        }
        this.currentIndex = 0;
    }

    /**
     * Updates the current animation or transition
     * If a transition is in progress, it will be updated
     * Otherwise, the current animation will be updated
     *
     * @param deltaTime The time since the last update
     */
    public void update(double deltaTime) {
        if (transition != null) {
            transition.update(deltaTime);
            if (transition.isComplete()) {
                transition = null;
            }
        } else {
            animations[currentIndex].update(deltaTime);
        }
    }

    /**
     * Draws the current animation or transition
     * If a transition is in progress, it will be drawn
     * Otherwise, the current animation will be drawn
     *
     * @param gc The GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        if (transition != null) {
            transition.draw(gc);
        } else {
            animations[currentIndex].draw(gc);
        }
    }

    /**
     * Starts a transition to the next animation
     * The transition will fade out the current animation and fade in the next one
     */
    public void nextAnimation() {
        int nextIndex = (currentIndex + 1) % animations.length;
        transition = new FadeTransition(animations[currentIndex], animations[nextIndex], 1.5, true);
        currentIndex = nextIndex;
    }

    /**
     * Gets the current animation
     * 
     * @return The current animation
     */
    public FrameAnimation getCurrentAnimation() {
        return animations[currentIndex];
    }

}
