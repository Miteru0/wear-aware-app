package ch.fhnw.team6.view;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackgroundPanelTest {

    private BackgroundPanel backgroundPanel;

    @BeforeEach
    void setUp() {

        Map<String, String[]> animationPaths = new HashMap<>();
        animationPaths.put("1", new String[] {
                "images/background/animation1/1.png",
                "images/background/animation1/2.png",
                "images/background/animation1/3.png",
                "images/background/animation1/4.png",
                "images/background/animation1/5.png",
                "images/background/animation1/6.png",
                "images/background/animation1/7.png",
                "images/background/animation1/8.png",
                "images/background/animation1/9.png",
                "images/background/animation1/10.png",
                "images/background/animation1/11.png",
                "images/background/animation1/12.png"
        });

        backgroundPanel = new BackgroundPanel(animationPaths, 100, "1");
    }

    @Test
    void testAnimationPathsLoadedSuccessfully() {
        assertNotNull(backgroundPanel);
        assertTrue(backgroundPanel.animations.containsKey("1"));
        assertEquals(12, backgroundPanel.animations.get("1").length); // Should have 12 frames
    }

    @Test
    void testAnimationUpdate() throws InterruptedException, InvocationTargetException {

        backgroundPanel.startAnimation();

        // Assert that the current frame is 0 initially
        assertEquals(0, backgroundPanel.currentFrame);

        Thread.sleep(150); // Sleep for a bit longer than the frame delay

        assertEquals(1, backgroundPanel.currentFrame);
    }

    @Test
    void testStopAnimation() throws InterruptedException {
        // Start the animation
        backgroundPanel.startAnimation();

        // Assert that the animation is running
        assertTrue(backgroundPanel.isAnimationRunning());

        // Stop the animation
        backgroundPanel.stopAnimation();

        // Assert that the animation has stopped
        assertFalse(backgroundPanel.isAnimationRunning());
    }

    @Test
    void testSetAnimation() {
        // Add another animation to test setAnimation
        Map<String, String[]> additionalAnimationPaths = new HashMap<>();
        additionalAnimationPaths.put("2", new String[] {
                "images/background/animation2/0.png",
        });

        // Initialize BackgroundPanel with the new animation paths
        BackgroundPanel newPanel = new BackgroundPanel(
            additionalAnimationPaths,
            100,
            "1");

        // Assert that the new animation is loaded
        assertNotNull(newPanel.animations.get("2"));
        assertEquals(1, newPanel.animations.get("2").length);

        // Set a new animation
        newPanel.setAnimation("2");

        // Assert that the current animation is changed
        assertEquals("2", newPanel.currentAnimation);
    }
}
