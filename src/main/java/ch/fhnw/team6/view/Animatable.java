package ch.fhnw.team6.view;

/**
 * Interface for animatable components.
 * Provides methods to control animation updates, start, stop, and set frame delay.
 */
public interface Animatable {

    void updateAnimation();

    void setAnimation(String name);

    boolean isAnimationRunning();

    void startAnimation();

    void stopAnimation();

    void setFrameDelay(int delay);

}
