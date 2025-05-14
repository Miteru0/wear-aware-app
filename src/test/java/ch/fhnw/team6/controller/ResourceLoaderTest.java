package ch.fhnw.team6.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.image.Image;

public class ResourceLoaderTest {

    @Test
    void testLoadMissingImageThrows() {
        assertThrows(RuntimeException.class, () -> {
            ResourceLoader.loadImage("/images/doesNotExist.png", 100, 100);
        });
    }

    @Test
    void testLoadAnimationFramesMissingImages() {
        assertThrows(RuntimeException.class, () -> {
            ResourceLoader.loadAnimationFrames("invalidFolder", "missingAnimation", 4, 100, 100);
        });
    }

    @Test
    void testLoadAnimationFramesReturnsCorrectLength() {
        Image[] frames = ResourceLoader.loadAnimationFrames("background", "test", 4, 100, 100);
        assertEquals(4, frames.length);
    }

    @Test
    void testLoadFlagImagesThrowsWhenMissing() {
        assertThrows(RuntimeException.class, () -> {
            ResourceLoader.loadFlagImages(100, 100, "/images/invalidFolder/");
        });
    }

}
