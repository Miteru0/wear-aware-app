package ch.fhnw.team6.view;

import io.github.humbleui.skija.Canvas;

public interface Animation {
    void update(double deltaTime);

    void draw(Canvas canvas);

    boolean isComplete();

    void reset();
}