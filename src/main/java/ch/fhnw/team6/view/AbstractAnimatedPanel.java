package ch.fhnw.team6.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class AbstractAnimatedPanel extends JPanel implements Animatable {

    protected Map<String, Image[]> animations = new HashMap<>();
    protected String currentAnimation = "idle";
    protected int currentFrame = 0;
    protected boolean running = false;
    protected int frameDelay = 100;
    private Timer animationTimer;

    /**
     * Constructor for AbstractAnimatedPanel
     * 
     * @param animationPaths   A map of animation names to their file paths
     * @param frameDelay       The delay between frames in milliseconds
     * @param defaultAnimation The default animation to play
     */
    public AbstractAnimatedPanel(Map<String, String[]> animationPaths, int frameDelay, String defaultAnimation) {
        this.currentAnimation = defaultAnimation != null ? defaultAnimation : "idle"; // Default to "idle" if null
        this.frameDelay = frameDelay > 0 ? frameDelay : 100;
        loadAnimations(animationPaths);
        animationTimer = new Timer(this.frameDelay, _ -> {
            updateAnimation();
            repaint();
            Toolkit.getDefaultToolkit().sync();  // Fixes the lagging issue
        });
    }

    /**
     * Constructor for AbstractAnimatedPanel
     * 
     * @param animationPaths A map of animation names to their file paths
     */
    public AbstractAnimatedPanel(Map<String, String[]> animationPaths) {
        this(animationPaths, 100, "idle");
    }

    /**
     * Load animations from the provided paths
     * @param animationPaths A map of animation names to their file paths
     */
    private void loadAnimations(Map<String, String[]> animationPaths) {
        if (animationPaths == null || animationPaths.isEmpty()) return;
    
        for (String key : animationPaths.keySet()) {
            String[] paths = animationPaths.get(key);
            if (paths == null || paths.length == 0) continue;
    
            Image[] frames = new Image[paths.length];
            for (int i = 0; i < paths.length; i++) {
                frames[i] = new ImageIcon(paths[i]).getImage();
            }
            animations.put(key, frames);
        }
    }
    

    /**
     * Update the current animation frame
     * This method is called by the Timer to update the animation frame
     * and repaint the panel.
     */
    @Override
    public void updateAnimation() {
        Image[] frames = animations.get(currentAnimation);
        if (frames != null) {
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    /**
     * Paint the current frame of the animation
     * @param g The Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image[] frames = animations.get(currentAnimation);
        if (frames != null && frames.length > 0) {
            g.drawImage(frames[currentFrame], 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Set the current animation
     * @param name The name of the animation to set
     */
    @Override
    public void setAnimation(String name) {
        if (animations.containsKey(name) && !currentAnimation.equals(name)) {
            currentAnimation = name;
            currentFrame = 0; // Reset to first frame
        }
    }

    /**
     * Check if the animation is running
     * @return true if the animation is running, false otherwise
     */
    @Override
    public boolean isAnimationRunning() {
        return animationTimer.isRunning();
    }

    /**
     * Start the animation
     * This method starts the Timer to update the animation frames.
     */
    @Override
    public void startAnimation() {
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    /**
     * Stop the animation
     * This method stops the Timer to stop updating the animation frames.
     */
    @Override
    public void stopAnimation() {
        animationTimer.stop();
    }

    /**
     * Set the delay between frames
     * @param delay The delay in milliseconds
     */
    @Override
    public void setFrameDelay(int delay) {
        frameDelay = delay;
        animationTimer.setDelay(frameDelay);
    }

}
