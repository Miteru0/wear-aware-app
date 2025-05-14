package ch.fhnw.team6.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextPaneTest {

    private TextPane textPane;
    private GraphicsContext gc;

    @BeforeEach
    void setUp() {
        textPane = new TextPane(0, 0, 200, 100);
        Canvas canvas = new Canvas(300, 300);
        gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font(24));
    }

    @Test
    void testCalculateAlignedX() {
        textPane.setPaddingX(10);
        textPane.setTextAlign(TextAlign.LEFT);
        assertEquals(10, textPane.calculateAlignedX(50));

        textPane.setTextAlign(TextAlign.CENTER);
        assertEquals((200 - 50) / 2, textPane.calculateAlignedX(50));

        textPane.setTextAlign(TextAlign.RIGHT);
        assertEquals(200 - 50 - 10, textPane.calculateAlignedX(50));
    }

    @Test
    void testCalculateAlignedY() {
        textPane.setPaddingY(20);
        textPane.setFontSize(24);

        textPane.setTextVAlign(TextVAlign.TOP);
        assertEquals(20 + 24, textPane.calculateAlignedY(50));

        textPane.setTextVAlign(TextVAlign.CENTER);
        assertEquals((100 - 50) / 2 + 24, textPane.calculateAlignedY(50));

        textPane.setTextVAlign(TextVAlign.BOTTOM);
        assertEquals(100 - 50 - 20 + 24, textPane.calculateAlignedY(50));
    }

    @Test
    void testGetSetFontSize() {
        textPane.setFontSize(30);
        assertEquals(30, textPane.getFontSize());
    }
}
