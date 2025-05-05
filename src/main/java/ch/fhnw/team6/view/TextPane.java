package ch.fhnw.team6.view;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.Rect;

public class TextPane {
    private float x, y, width, height;
    private String question;
    private String answer;
    private boolean isInitialized = false;
    private int fontSize = 24;

    public TextPane(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.question = "";
        this.answer = "";
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void draw(Canvas canvas) {
        try (Paint panePaint = new Paint();
                Paint textPaint = new Paint()) {

            // Pane background
            panePaint.setColor(0xFF222222);
            canvas.drawRect(Rect.makeXYWH(x, y, width, height), panePaint);

            // Text color
            textPaint.setColor(0xFFFFFFFF);

            // Font setup
            Font font = new Font(Typeface.makeDefault(), fontSize);

            // Measure question text
            float questionWidth = font.measureText(question, textPaint).getWidth();
            float questionX = x + (width - questionWidth) / 2;
            float questionY = y + 50;

            // Measure answer text
            float answerWidth = font.measureText(answer, textPaint).getWidth();
            float answerX = x + (width - answerWidth) / 2;
            float answerY = y + 100;

            // Draw both texts centered
            canvas.drawString(question, questionX, questionY, font, textPaint);
            canvas.drawString(answer, answerX, answerY, font, textPaint);
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
