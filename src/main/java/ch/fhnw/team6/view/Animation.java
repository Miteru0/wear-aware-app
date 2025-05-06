package ch.fhnw.team6.view;

import javafx.scene.canvas.GraphicsContext;

public interface Animation {
    void update(double deltaTime);
    void draw(GraphicsContext gc);
    boolean isComplete();
    void reset();
}
