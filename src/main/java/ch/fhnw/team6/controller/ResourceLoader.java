package ch.fhnw.team6.controller;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.fhnw.team6.model.Language;

public class ResourceLoader {

    /**
     * Loads an array of images as animation frames.
     *
     * @param animationFolderName The folder name inside images/
     * @param animationName The name of the animation
     * @param frameCount   The number of frames
     * @param targetWidth  The target width for scaling
     * @param targetHeight The target height for scaling
     * 
     * @return Array of Images.
     */
    public static Image[] loadAnimationFrames(String animationFolderName, String animationName, int frameCount, double targetWidth, double targetHeight) {
        Image[] frames = new Image[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String path = String.format("/images/%s/%s/%d.png", animationFolderName, animationName, i);
            frames[i - 1] = loadImage(path, targetWidth, targetHeight);
            System.out.println("Loaded frame: " + path);
        }
        return frames;
    }

    /**
     * Loads an image from resources.
     *
     * @param path The resource path to the image.
     * @return The loaded Image.
     */
    public static Image loadImage(String path, double targetWidth, double targetHeight) {
        try (InputStream stream = ResourceLoader.class.getResourceAsStream(path)) {
            if (stream == null)
                throw new RuntimeException("Resource not found: " + path);
                    
            // Scale down while loading to avoid giant heap footprint
            return new Image(stream, targetWidth, targetHeight, false, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + path, e);
        }
    }

    // /**
    //  * Loads an image from resources without scaling.
    //  *
    //  * @param path The resource path to the image.
    //  * @return The loaded Image.
    //  */
    // private static Image loadImage(String path) {
    //     try (InputStream stream = ResourceLoader.class.getResourceAsStream(path)) {
    //         if (stream == null)
    //             throw new RuntimeException("Resource not found: " + path);
                    
    //         return new Image(stream);
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to load image: " + path, e);
    //     }
    // }


    /**
     * Loads flag images for all supported languages.
     *
     * @return A map of Language to Image.
     */
    public static Map<Language, Image> loadFlagImages(double width, double height) {
        Map<Language, Image> flags = new LinkedHashMap<>();
        for (Language lang : Language.values()) {
            String path = String.format("/images/flags/%s.png", lang.name().toLowerCase());
            flags.put(lang, loadImage(path, width, height));
            System.out.println("Loaded flag: " + path);
        }
        return flags;
    }
    
}
