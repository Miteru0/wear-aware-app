package ch.fhnw.team6.view;

import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import ch.fhnw.team6.controller.ResourceLoader;

public class AnimationManager {

    private final Window window; // The window to draw on
    private final Animation[] animations; // Array of animations
    private int currentIndex; // Current animation index
    private Animation transition; // Current transition animation

    /**
     * Constructor for AnimationManager
     * Initializes the animations and sets the current index to 0
     *
     * @param window The window to draw on
     */
    public AnimationManager(Window window) {
        this.window = window;
        this.animations = new Animation[7];
        for (int i = 0; i < 7; i++) {
            String name = "animation" + (i + 1);
            animations[i] = new FrameAnimation(window, ResourceLoader.loadAnimationFrames(name, 4000, 2255), 4);
        }
    }

    /**
     * Updates the current animation or transition
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
     * Draws the current animation or transition on the canvas
     *
     * @param canvas The canvas to draw on
     */
    public void draw(Canvas canvas) {
        if (transition != null) {
            transition.draw(canvas);
        } else {
            animations[currentIndex].draw(canvas);
        }
    }

    /**
     * Gets the next animation
     */
    public void nextAnimation() {
        int nextIndex = (currentIndex + 1) % animations.length;
        transition = new FadeTransition(window, animations[currentIndex], animations[nextIndex]);
        currentIndex = nextIndex;
    }

}