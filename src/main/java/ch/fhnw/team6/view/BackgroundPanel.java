package ch.fhnw.team6.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BackgroundPanel extends AbstractAnimatedPanel {

    private static final String ANIMATION_PATH = "src/test/resources/images/background";

    /**
     * Get the paths of the animation files
     * @return A map of animation names to their file paths
     */
    private static Map<String, String[]> getAnimationPaths() {
        Map<String, String[]> animations = new HashMap<>();
        animations.put("1", getAnimationPaths("animation1"));
        return animations;
    }


    /**
     * Get the paths of the animation files
     * @param animationName The name of the animation
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
     * Constructor for BackgroundPanel
     * Loads the animation paths and initializes the panel
     */
    public BackgroundPanel() {
        super(getAnimationPaths(), 100, "1");
    }

    /**
     * Constructor for BackgroundPanel
     * For testing purposes only
     * @param animationPaths
     */
    public BackgroundPanel(Map<String, String[]> animationPaths, int frameDelay, String defaultAnimation) {
        super(animationPaths, frameDelay, defaultAnimation);
    }

}
