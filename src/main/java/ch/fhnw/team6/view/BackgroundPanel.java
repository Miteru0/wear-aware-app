package ch.fhnw.team6.view;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BackgroundPanel extends AbstractAnimatedPanel {

    private static final String ANIMATION_PATH = "src/test/resources/images/background";

    // Transition-related fields:
    private boolean transitioning = false;
    private float transitionAlpha = 0f;
    private String nextAnimationKey = null;
    private Timer transitionTimer;
    private int transitionDuration = 1000;
    private int transitionSteps = 20;

    /**
     * Get the paths of the animation files
     * @return A map of animation names to their file paths
     */
    private static Map<String, String[]> getAnimationPaths() {
        Map<String, String[]> animations = new HashMap<>();
        animations.put("1", getAnimationPaths("animation1"));
        animations.put("2", getAnimationPaths("animation2"));
        animations.put("3", getAnimationPaths("animation3"));
        animations.put("4", getAnimationPaths("animation4"));
        return animations;
    }

    /**
     * Get the paths of the animation files for a given animation name.
     * @param animationName The name of the animation folder
     * @return An array of file paths for the animation frames
     */
    private static String[] getAnimationPaths(String animationName) {
        File animationDir = new File(ANIMATION_PATH + File.separator + animationName);
        if (!animationDir.exists() || !animationDir.isDirectory()) {
            throw new IllegalArgumentException("Animation directory not found: " + animationDir.getAbsolutePath());
        }
        String[] fileNames = animationDir.list();
        if (fileNames == null) {
            throw new IllegalArgumentException("No files found in animation directory: " + animationDir.getAbsolutePath());
        }
        String[] result = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            result[i] = animationDir + File.separator + fileNames[i];
        }
        return result;
    }

    /**
     * Constructor for BackgroundPanel using default animations.
     */
    public BackgroundPanel() {
        super(getAnimationPaths(), 100, "1");
        startRepaintTimer();
    }

    /**
     * Constructor for BackgroundPanel.
     * @param animationPaths A map of animation names to their file paths.
     * @param frameDelay Delay between frames in milliseconds.
     * @param defaultAnimation The animation key to use initially.
     */
    public BackgroundPanel(Map<String, String[]> animationPaths, int frameDelay, String defaultAnimation) {
        super(animationPaths, frameDelay, defaultAnimation);
        startRepaintTimer();
    }

    /**
     * Starts a small timer that repaints the panel every 30ms.
     */
    private void startRepaintTimer() {
        new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }).start();
    }

    /**
     * Initiates a crossfade transition to the new animation identified by newAnimationKey.
     * @param newAnimationKey The key of the new animation to transition to.
     */
    public void startTransitionTo(String newAnimationKey) {
        if (transitioning || newAnimationKey.equals(currentAnimation)) return;

        this.nextAnimationKey = newAnimationKey;
        transitioning = true;
        transitionAlpha = 0f;

        if (transitionTimer != null) {
            transitionTimer.stop();
        }
        int timerDelay = transitionDuration / transitionSteps;
        transitionTimer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transitionAlpha += 1.0f / transitionSteps;
                if (transitionAlpha >= 1.0f) {
                    transitionAlpha = 1.0f;
                    transitioning = false;
                    setAnimation(nextAnimationKey);
                    nextAnimationKey = null;
                    transitionTimer.stop();
                }
                repaint();
            }
        });
        transitionTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (!transitioning) {
            Image[] frames = animations.get(currentAnimation);
            if (frames != null && frames.length > 0) {
                g2d.drawImage(frames[currentFrame], 0, 0, getWidth(), getHeight(), this);
            }
        } else {
            Image[] currFrames = animations.get(currentAnimation);
            Image[] nextFrames = animations.get(nextAnimationKey);
            if (currFrames != null && currFrames.length > 0 && nextFrames != null && nextFrames.length > 0) {
                // Draw current animation frame with fading-out opacity.
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - transitionAlpha));
                g2d.drawImage(currFrames[currentFrame], 0, 0, getWidth(), getHeight(), this);

                int nextFrameIndex = currentFrame % nextFrames.length;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionAlpha));
                g2d.drawImage(nextFrames[nextFrameIndex], 0, 0, getWidth(), getHeight(), this);
            }
        }
        g2d.dispose();
    }
}
