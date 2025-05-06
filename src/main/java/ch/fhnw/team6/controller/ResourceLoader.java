package ch.fhnw.team6.controller;

import io.github.humbleui.skija.*;
import java.io.InputStream;

public class ResourceLoader {

    /**
     * Loads an image from the specified path and scales it to the target
     * dimensions.
     *
     * @param path         The path to the image file.
     * @param targetWidth  The desired width of the scaled image.
     * @param targetHeight The desired height of the scaled image.
     * @return The scaled image.
     */
    public static Image[] loadAnimationFrames(String animationName, int targetWidth, int targetHeight) {

        Image[] frames = new Image[2];
        for (int i = 1; i <= 2; i++) {
            String path = String.format("images/background/%s/%d.png", animationName, i);
            frames[i - 1] = loadAndScaleImage(path, targetWidth, targetHeight);
            System.out.println("Loaded frame: " + path);
        }
        return frames;

    }

    /**
     * Loads and scales an image from the specified path.
     *
     * @param path         The path to the image file.
     * @param targetWidth  The desired width of the scaled image.
     * @param targetHeight The desired height of the scaled image.
     * @return The scaled image.
     */
    private static Image loadAndScaleImage(String path, int targetWidth, int targetHeight) {

        try (InputStream stream = ResourceLoader.class.getResourceAsStream("/" + path)) {
            if (stream == null)
                throw new RuntimeException("Resource not found: " + path);

            // Load original image
            byte[] bytes = stream.readAllBytes();
            Image originalImage = Image.makeDeferredFromEncodedBytes(bytes);
            if (originalImage == null)
                throw new RuntimeException("Failed to decode image: " + path);

            // Create target surface with transparent background
            ImageInfo imageInfo = new ImageInfo(targetWidth, targetHeight, ColorType.RGBA_8888, ColorAlphaType.PREMUL);
            Surface surface = Surface.makeRaster(imageInfo, targetWidth * 4L);
            Canvas canvas = surface.getCanvas();

            // Clear with transparent black (ARGB 0x00000000)
            canvas.clear(0x00000000);

            // Calculate perfect scaling while maintaining aspect ratio
            float scale = Math.min(
                    (float) targetWidth / originalImage.getWidth(),
                    (float) targetHeight / originalImage.getHeight());

            // Calculate precise dimensions and position
            float scaledWidth = originalImage.getWidth() * scale;
            float scaledHeight = originalImage.getHeight() * scale;
            float x = (targetWidth - scaledWidth) / 2f;
            float y = (targetHeight - scaledHeight) / 2f;

            // Draw with perfect alignment
            canvas.save();
            canvas.translate(x, y);
            canvas.scale(scale, scale);
            canvas.drawImage(originalImage, 0, 0);
            canvas.restore();

            Image result = surface.makeImageSnapshot();
            surface.close();
            originalImage.close();

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load or scale: " + path, e);
        }
    }
}